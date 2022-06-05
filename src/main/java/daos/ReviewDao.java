package daos;

import entities.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ReviewDao {
    public final Connection CONN;


    public ReviewDao(Connection conn) {
        CONN = conn;
    }

    /**
     * Метод, создающий таблицу отзывов в БД (если такая не определена) с
     * использованием sql-запроса.
     * Определены такие поля сущности: id (первичный ключ, автоинкремент),
     * текст отзыва (строка), id пользователя (внешний ключ сущности Пользователь, число)
     */

    public void createTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS reviews (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "text VARCHAR(100), " +
                    "user_id INTEGER"+
                    ")");
        }
    }

    /**
     * Метод, удаляющий таблицу отзывов в БД с
     * использованием sql-запроса.
     */

    public void dropTable() throws SQLException {
        try (Statement statement = CONN.createStatement()) {
            statement.executeUpdate("DROP TABLE reviews");
        }
    }

    /**
     * Метод, возвращающий все записи с таблицы отзывов в виде коллекции обьектов Отзыв
     */

    public Collection<Review> getAll() throws SQLException {
        Collection<Review> reviews = new ArrayList<>();
        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery("SELECT * FROM reviews");
            while (cursor.next()) {
                reviews.add(createReviewFromCursor(cursor));
            }
        }
        return reviews;
    }

    /**
     * Вспомогательный метод, формирующий с записи таблицы отзывов данную сущность
     */

    private Review createReviewFromCursor(ResultSet cursor) throws SQLException {
        Review review = new Review();
        review.id = cursor.getInt("id");
        review.userId = cursor.getInt("user_id");
        review.text = cursor.getString("text");
        return review;
    }

    /**
     * Метод, вовзращающий с таблицы отзывов данный обьект по указанному id
     * Использование sql-запроса
     */

    public Optional<Review> getById(int id) throws SQLException {

        try (Statement statement = CONN.createStatement()) {
            ResultSet cursor = statement.executeQuery(
                    String.format("SELECT * FROM reviews WHERE id = %d", id));
            if (cursor.next()) {
                return Optional.of(createReviewFromCursor(cursor));
            } else {
                return Optional.empty();
            }
        }
    }


    /**
     * Метод, обновляющий информацию необходимой записи в таблице отзывов
     * Принимает в параметре обьект Отзыв, откуда берет необходимые поля для обновления
     * Обьект уже должен быть определен как запись в таблице (id должны совпадать)
     */

    public void update(Review review) throws SQLException, IllegalArgumentException {

        if (review.id == 0) {
            throw new IllegalArgumentException("ID is not initialized");
        }

        String sql = "UPDATE reviews SET user_id = ?," +
                " text = ? " +
                " WHERE id = ?";

        try (PreparedStatement statement = CONN.prepareStatement(sql)) {
            statement.setInt(1, review.userId);
            statement.setString(2, review.text);
            statement.setInt(3, review.id);
            statement.executeUpdate();
        }

    }

    /**
     * Метод, создающий запись в таблице отзывов
     */

    public void insert(Review review) throws SQLException, IllegalArgumentException {

        if (review.id != 0) {
            throw new IllegalArgumentException("ID has to be 0");
        }

        final String sql = "INSERT INTO reviews (user_id, text) VALUES(?,?)";
        try (PreparedStatement statement = CONN.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, review.userId);
            statement.setString(2, review.text);
            int affectedRows = statement.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Creating review failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    review.id = generatedKeys.getInt(1);

                }else {
                    throw new SQLException("Creating reviews failed, no ID obtained");
                }
            }
        }
    }

}
