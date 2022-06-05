package daos;

import entities.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class AuthorDao {
    private final Connection CONN;

    public AuthorDao(Connection conn) {
        CONN = conn;
    }

    /**
     * Метод, создающий таблицу авторов в БД (если такая не определена) с
     * использованием sql-запроса.
     * Определены такие поля сущности: id (первичный ключ, автоинкремент),
     * имя (строка), год рождения(число)
     */

    public void createTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS author (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(100)," +
                    "birth_year INTEGER" +
                    ")");
        }
    }

    /**
     * Метод, удаляющий таблицу авторов в БД с
     * использованием sql-запроса.
     */

    public void dropTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("DROP TABLE author");
        }
    }

    /**
     * Метод, возвращающий все записи с таблицы авторов в виде коллекции обьектов Автор
     */

    public Collection<Author> getAll() throws SQLException {
        Collection<Author> authors = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery("SELECT * FROM author");
            while (cursor.next()) {
                authors.add(createAuthorFromCursor(cursor));
            }
        }
        return authors;
    }

    /**
     * Метод, возвращающий с таблицы авторов данный обьект по указанному id
     * Использование sql-запроса
     */

    public Optional<Author> getById(int id) throws SQLException {
        Collection<Author> authors = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM author WHERE id = %d", id));
            if (cursor.next()) {
                return Optional.of(createAuthorFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Вспомогательный метод, формирующий с записи таблицы авторов данную сущность
     */

    private Author createAuthorFromCursor(ResultSet cursor) throws SQLException {
        Author author = new Author();
        author.id = cursor.getInt("id");
        author.name = cursor.getString("name");
        author.yearOfBirth = cursor.getInt("birth_year");
        return author;
    }

    /**
     * Метод, возвращающий с таблицы авторов данный обьект по указанному имени
     * Использование sql-запроса
     */

    public Optional<Author> findByName(String text) throws SQLException {
       // Collection<Author> authors = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM author WHERE name LIKE  '%%%s%%'", text));
            if (cursor.next()) {
                return Optional.of(createAuthorFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Метод, обновляющий информацию необходимой записи в таблице авторов
     * Принимает в параметре обьект Автор, откуда берет необходимые поля для обновления
     * Обьект уже должен быть определен как запись в таблице (id должны совпадать)
     */

    public void update(Author author) throws SQLException, IllegalArgumentException {

        if (author.id == 0) {
            throw new IllegalArgumentException("ID is not initialized");
        }

        String sql = "UPDATE author SET name = ?, birth_year = ?" +
                " WHERE id = ?";

        try (PreparedStatement statement = CONN.prepareStatement(sql)) {
            statement.setString(1, author.name);
            statement.setInt(2, author.yearOfBirth);
            statement.setInt(3, author.id);
            statement.executeUpdate();
        }

    }

    /**
     * Метод, создающий запись в таблице авторов
     */

    public void insert(Author author) throws SQLException, IllegalArgumentException {

        if (author.id != 0) {
            throw new IllegalArgumentException("ID has to be 0");
        }

        final String sql = "INSERT INTO author (name, birth_year) VALUES(?,?)";
        try (PreparedStatement statement = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.name);
            statement.setInt(2, author.yearOfBirth);
            int affectedRows = statement.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Creating author failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    author.id = generatedKeys.getInt(1);

                }else {
                    throw new SQLException("Creating author failed, no ID obtained");
                }
            }
        }
    }


}
