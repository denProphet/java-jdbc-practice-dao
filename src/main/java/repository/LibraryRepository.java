package repository;

import daos.AuthorDao;
import daos.BookDao;
import daos.ReviewDao;
import daos.UserDao;
import entities.Author;
import entities.Book;
import entities.Review;
import entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;


public class LibraryRepository implements ILibraryRepository {
    private final AuthorDao authorDao;
    private final BookDao bookDao;
    private final ReviewDao reviewDao;
    private final UserDao userDao;

    public LibraryRepository(AuthorDao authorDao, BookDao bookDao, ReviewDao reviewDao, UserDao userDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
        this.reviewDao = reviewDao;
        this.userDao = userDao;
    }

    @Override
    public Collection<Book> getAllBooks() {
        try {
            return bookDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get books", e);
        }
    }

    @Override
    public Collection<Author> getAllAuthors() {
        try {
            return authorDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get authors", e);
        }
    }

    @Override
    public void saveAuthor(Author author) {
        if (author.id == 0) {
            try {
                authorDao.insert(author);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save author", e);
            }
        } else {
            try {
                authorDao.update(author);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update author", e);
            }
        }

    }

    @Override
    public void saveBook(Book book, Author author) {
        saveAuthor(author);
        book.authorId = author.id;

        if (book.id == 0) {
            try {
                bookDao.insert(book);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save book", e);
            }
        } else {
            try {
                bookDao.update(book);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update book", e);
            }
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        try {
            return userDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get users", e);
        }
    }

    @Override
    public Collection<Review> getAllReviews() {
        try {
            return reviewDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get reviews", e);
        }
    }

    @Override
    public void saveUser(User user) {
        if (user.id == 0) {
            try {
                userDao.insert(user);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save user", e);
            }
        } else {
            try {
                userDao.update(user);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update user", e);
            }
        }
    }

    @Override
    public void saveReview(Review review, User user) {
        saveUser(user);
        review.userId = user.id;

        if (review.id == 0) {
            try {
                reviewDao.insert(review);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save review", e);
            }
        } else {
            try {
                reviewDao.update(review);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update review", e);
            }
        }
    }

    public static ILibraryRepository getInitializedLibraryRepository
            (Connection connection) throws  SQLException{
        BookDao bookDao = new BookDao(connection);
        ReviewDao reviewDao = new ReviewDao(connection);
        UserDao userDao = new UserDao(connection);
        AuthorDao authorDao = new AuthorDao(connection);

            bookDao.createTable();
            authorDao.createTable();
            reviewDao.createTable();
            userDao.createTable();

            return new LibraryRepository(authorDao,bookDao,reviewDao,userDao);

    }
}
