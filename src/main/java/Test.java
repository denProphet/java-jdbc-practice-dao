import daos.AuthorDao;
import daos.BookDao;
import daos.ReviewDao;
import daos.UserDao;
import entities.Author;
import entities.Book;
import entities.Review;
import entities.User;
import repository.ILibraryRepository;
import repository.LibraryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Класс, где содержатся методы для тестирования
 * обьектов классов *Dao и *Repository
 * */

public class Test {
    private final Collection<User> users = new ArrayList<>();
    private final Collection<Book> books = new ArrayList<>();
    private final Collection<Author> authors = new ArrayList<>();
    private final Collection<Review> reviews = new ArrayList<>();

    {
        users.addAll(Arrays.asList(
                new User("Николай"),
                new User("Валентина"),
                new User("Людмила"),
                new User("Максим"),
                new User("Галина")
        ));

        reviews.addAll(Arrays.asList(
                new Review(1, "text1"),
                new Review(2, "text2"),
                new Review(3, "text3"),
                new Review(4, "text4"),
                new Review(5, "text5"),
                new Review(5, "text6"),
                new Review(2, "text7")
        ));

        authors.addAll(Arrays.asList(
                new Author("Жюль Верн", 1750),
                new Author("Николай Гоголь", 1950),
                new Author("Тарас Шевченко", 1820)
        ));

        books.addAll(Arrays.asList(
                new Book("Таинственный остров", 1800, 1),
                new Book("Вий", 1900, 2),
                new Book("Небо", 1700, 1),
                new Book("Страна", 1720, 3)
        ));
    }

    /**
     * Тестирование таблицы авторов
     * с методами создания, вставки, печати, поиска и обновления сущностей
     * */

    void authorTest(Connection connection) {
        AuthorDao authorDao = new AuthorDao(connection);
        try {
            authorDao.createTable();

            for (Author author : authors) {
                authorDao.insert(author);
            }
            System.out.println("\n++++++Creating db, inserting + printing test results++++++");
            authorDao.getAll().forEach(System.out::println);
            System.out.println("\n++++++Finding by name test results++++++");
            System.out.println(authorDao.findByName("Жюль Верн"));
            System.out.println("\n++++++Getting by id test results++++++");
            System.out.println(authorDao.getById(3));

            Author authorToUpdate = new Author("Виктор Петров", 2002);
            authorToUpdate.id = 2;
            authorDao.update(authorToUpdate);
            System.out.println("\n++++++Printing with updated value test results++++++");
            authorDao.getAll().forEach(System.out::println);

            authorDao.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Тестирование таблицы пользователей
     * с методами создания, вставки, печати, поиска и обновления сущностей
     * */

    void userTest(Connection connection) {
        UserDao userDao = new UserDao(connection);
        try {
            userDao.createTable();

            for (User user : users) {
                userDao.insert(user);
            }

            System.out.println("\n++++++Creating db, inserting + printing test results++++++");
            userDao.getAll().forEach(System.out::println);
            System.out.println("\n++++++Finding by name test results++++++");
            System.out.println("\n" + userDao.findByName("Людмила"));
            System.out.println("\n++++++Getting by id test results++++++");
            System.out.println("\n" + userDao.getById(4)); //Максим

            User userToUpdate = new User("Катя Дербенева");
            userToUpdate.id = 2; //Катя Дербенева вместо Валентины (id=2)
            userDao.update(userToUpdate);

            System.out.println("\n++++++Printing with updated value test results++++++");
            userDao.getAll().forEach(System.out::println);

            userDao.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Тестирование таблицы книг
     * с методами создания, вставки, печати, поиска и обновления сущностей
     * */

    void bookTest(Connection connection) {
        BookDao bookDao = new BookDao(connection);
        try {
            bookDao.createTable();

            for (Book book : books) {
                bookDao.insert(book);
            }
            System.out.println("\n++++++Creating db, inserting + printing test results++++++");
            bookDao.getAll().forEach(System.out::println);
            System.out.println("\n++++++Finding by name test results++++++");
            System.out.println("\n" + bookDao.findByName("Страна"));
            System.out.println("\n++++++Getting by id test results++++++");
            System.out.println("\n" + bookDao.getById(2));//Вий

            Book bookToUpdate = new Book("На другой планете", 1956, 3);
            bookToUpdate.id = 3;
            bookDao.update(bookToUpdate); //"На другой планете" вместо "Небо"

            System.out.println("\n++++++Printing with updated value test results++++++");
            bookDao.getAll().forEach(System.out::println);

            bookDao.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Тестирование таблицы отзывов
     * с методами создания, вставки, печати, обновления сущностей
     * */

    void reviewTest(Connection connection) {
        ReviewDao reviewDao = new ReviewDao(connection);
        try {
            reviewDao.createTable();

            for (Review review : reviews) {
                reviewDao.insert(review);
            }
            System.out.println("\n++++++Creating db, inserting + printing test results++++++");
            reviewDao.getAll().forEach(System.out::println);

            System.out.println("\n++++++Getting by id test results++++++");
            System.out.println("\n" + reviewDao.getById(2));

            Review reviewToUpdate = new Review(3, "updatedText");
            reviewToUpdate.id = 3;
            reviewDao.update(reviewToUpdate);

            System.out.println("\n++++++Printing with updated value test results++++++");
            reviewDao.getAll().forEach(System.out::println);

            reviewDao.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Тестирование осн. методов LibraryRepository
     * */

    void LibraryRepositoryTest(Connection connection) {
        try {
            ILibraryRepository libraryRepository =
                    LibraryRepository.getInitializedLibraryRepository(connection);


            System.out.println("\n++++++inserting items + printing test results++++++");
            Author author = new Author("Лермонтов", 1814);
            Book book = new Book("Таинственный остров", 1800, 1);
            libraryRepository.saveBook(book, author);
            libraryRepository.getAllBooks().forEach(System.out::println);

            User user = new User("Ярослав");
            Review review = new Review(18, "text18");
            libraryRepository.saveReview(review, user);
            libraryRepository.getAllReviews().forEach(System.out::println);

            System.out.println("\n++++++updating items + printing test results++++++");

            book.name = "Таинственный остров Расширенное издание";
            libraryRepository.saveBook(book, author);
            libraryRepository.getAllBooks().forEach(System.out::println);


            review.text = "text18 updated";
            libraryRepository.saveReview(review, user);
            libraryRepository.getAllReviews().forEach(System.out::println);


            dropAllTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropAllTables(Connection connection) throws SQLException {
        new AuthorDao(connection).dropTable();
        new BookDao(connection).dropTable();
        new UserDao(connection).dropTable();
        new ReviewDao(connection).dropTable();
    }


}
