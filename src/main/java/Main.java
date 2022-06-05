
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {
    private static final String SQLITE_CONNECTION_STRING = "jdbc:sqlite:sample.db";

    public static void main(String[] args) {
        Main main = new Main();
        try (Connection connection =
                     DriverManager.getConnection(SQLITE_CONNECTION_STRING)) {
            main.task(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void task(Connection connection) {

        Test test = new Test();

        System.out.println("\n==Books===========================================");
        test.bookTest(connection);
        System.out.println("\n==Reviews=========================================");
        test.reviewTest(connection);
        System.out.println("\n==Users===========================================");
        test.userTest(connection);
        System.out.println("\n==Authors=========================================");
        test.authorTest(connection);
        System.out.println("\n==Repository=========================================");
        test.LibraryRepositoryTest(connection);
    }


}
