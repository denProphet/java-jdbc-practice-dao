package daos;

import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class UserDao {
    private final Connection CONN;

    public UserDao(Connection conn) {
        CONN = conn;
    }

    /**
     * Метод, создающий таблицу пользователей в БД (если такая не определена) с
     * использованием sql-запроса.
     * Определены такие поля сущности: id (первичный ключ, автоинкремент),
     * имя (строка)
     */

    public void createTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(100)" +
                    ")");
        }
    }

    /**
     * Метод, удаляющий таблицу пользователей в БД с
     * использованием sql-запроса.
     */

    public void dropTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("DROP TABLE users");
        }
    }

    /**
     * Метод, возвращающий все записи с таблицы пользователей в виде коллекции обьектов Пользователь
     */

    public Collection<User> getAll() throws SQLException {
        Collection<User> users = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery("SELECT * FROM users");
            while (cursor.next()) {
                users.add(createUserFromCursor(cursor));
            }
        }
        return users;
    }

    /**
     * Метод, возращающий с таблицы пользователей данный обьект по указанному id
     * Использование sql-запроса
     */

    public Optional<User> getById(int id) throws SQLException {

        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM users WHERE id = %d", id));
            if (cursor.next()) {
                return Optional.of(createUserFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Вспомогательный метод, формирующий с записи таблицы пользователей данную сущность
     */

    private User createUserFromCursor(ResultSet cursor) throws SQLException {
        User user = new User();
        user.id = cursor.getInt("id");
        user.name = cursor.getString("name");
        return user;
    }

    /**
     * Метод, возвращающий с таблицы пользователей данный обьект по указанному имени
     * Использование sql-запроса
     */

    public Optional<User> findByName(String text) throws SQLException {

        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM users WHERE name LIKE  '%%%s%%'", text));
            if (cursor.next()) {
                return Optional.of(createUserFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Метод, обновляющий информацию необходимой записи в таблице пользователей
     * Принимает в параметре обьект Пользователь, откуда берет необходимые поля для обновления
     * Обьект уже должен быть определен как запись в таблице (id должны совпадать)
     */

    public void update(User user) throws SQLException, IllegalArgumentException {

        if (user.id == 0) {
            throw new IllegalArgumentException("ID is not initialized");
        }

        String sql = "UPDATE users SET name = ?" +
                " WHERE id = ?";

        try (PreparedStatement statement = CONN.prepareStatement(sql)) {
            statement.setString(1, user.name);
            statement.setInt(2, user.id);
            statement.executeUpdate();
        }

    }

    /**
     * Метод, создающий запись в таблице пользователей
     */

    public void insert(User user) throws SQLException, IllegalArgumentException {

        if (user.id != 0) {
            throw new IllegalArgumentException("ID has to be 0");
        }

        final String sql = "INSERT INTO users (name) VALUES(?)";
        try (PreparedStatement statement = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.name);
            int affectedRows = statement.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Creating user failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    user.id = generatedKeys.getInt(1);

                }else {
                    throw new SQLException("Creating user failed, no ID obtained");
                }
            }
        }
    }

}
