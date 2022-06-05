package entities;

public class Book {

    public int id;
    public String name;
    public int yearOfPublication;
    public int authorId;

    public Book(String name, int yearOfPublication, int authorId) {

        this.name = name;
        this.yearOfPublication = yearOfPublication;
        this.authorId = authorId;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", authorId=" + authorId +
                '}';
    }

}
