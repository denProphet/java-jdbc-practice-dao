package repository;

import entities.Author;
import entities.Book;
import entities.Review;
import entities.User;

import java.util.Collection;

public interface ILibraryRepository {

    Collection<Book> getAllBooks();
    Collection<Author> getAllAuthors();
    void  saveAuthor(Author author);
    void saveBook(Book book, Author author);

    Collection<User> getAllUsers();
    Collection<Review> getAllReviews();
    void  saveUser(User user);
    void saveReview(Review review, User user);


}
