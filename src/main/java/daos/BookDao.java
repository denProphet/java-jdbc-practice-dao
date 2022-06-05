package daos;

import entities.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class BookDao {

    public final Connection CONN;


    public BookDao(Connection conn) {
        CONN = conn;
    }

    /**
     * Метод, создающий таблицу книг в БД (если такая не определена) с
     * использованием sql-запроса.
     * Определены такие поля сущности: id (первичный ключ, автоинкремент),
     * имя (строка), год публикации (число), id автора (внешний ключ сущности Автор, число)
     */

    public void createTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS book" +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name VARCHAR(100)," +
                    " publication_year INTEGER," +
                    " author_id INTEGER)");
        }
    }

    /**
     * Метод, удаляющий таблицу книг в БД с
     * использованием sql-запроса.
     */

    public void dropTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("DROP TABLE book");
        }
    }

    /**
     * Метод, возвращающий все записи с таблицы книг в виде коллекции обьектов Книга
     */

    public Collection<Book> getAll() throws SQLException {
        Collection<Book> books = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery("SELECT * FROM book");
            while (cursor.next()) {
                books.add(createBookFromCursor(cursor));
            }
        }
        return books;
    }

    /**
     * Вспомогательный метод, формирующий с записи таблицы книг данную сущность
     */

    private Book createBookFromCursor(ResultSet cursor) throws SQLException {
        Book book = new Book();
        book.id = cursor.getInt("id");
        book.name = cursor.getString("name");
        book.yearOfPublication = cursor.getInt("publication_year");
        book.authorId = cursor.getInt("author_id");
        return book;
    }

    /**
     * Метод, возвращающий с таблицы книг данный обьект по указанному id
     * Использование sql-запроса
     */

    public Optional<Book> getById(int id) throws SQLException {
        Collection<Book> books = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM book WHERE id = %d", id));
            if (cursor.next()) {
                return Optional.of(createBookFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Метод, возвращающий с таблицы книг данный обьект по указанному имени
     * Использование sql-запроса
     */

    public Optional<Book> findByName(String text) throws SQLException {
        Collection<Book> books = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM book WHERE name LIKE  '%%%s%%'", text));
            if (cursor.next()) {
                return Optional.of(createBookFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Метод, обновляющий информацию необходимой записи в таблице книг
     * Принимает в параметре обьект книгу, откуда берет необходимые поля для обновления
     * Обьект уже должен быть определен как запись в таблице (id должны совпадать)
     */

    public void update(Book book) throws SQLException, IllegalArgumentException {

        if (book.id == 0) {
            throw new IllegalArgumentException("ID is not initialized");
        }

        String sql = "UPDATE book SET name = ?," +
                " publication_year = ?," +
                " author_id = ?" +
                " WHERE id = ?";

        try (PreparedStatement statement = CONN.prepareStatement(sql)) {
            statement.setString(1, book.name);
            statement.setInt(2, book.yearOfPublication);
            statement.setInt(3, book.authorId);
            statement.setInt(4, book.id);
            statement.executeUpdate();
        }

    }

    /**
     * Метод, создающий запись в таблице книг
     */

    public void insert(Book book) throws SQLException, IllegalArgumentException {

        if (book.id != 0) {
            throw new IllegalArgumentException("ID has to be 0");
        }

        final String sql = "INSERT INTO book (name, publication_year, author_id) VALUES(?,?,?)";
        try (PreparedStatement statement = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.name);
            statement.setInt(2, book.yearOfPublication);
            statement.setInt(3, book.authorId);
            int affectedRows = statement.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Creating book failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    book.id = generatedKeys.getInt(1);

                }else {
                    throw new SQLException("Creating book failed, no ID obtained");
                }
            }
        }
    }



}
