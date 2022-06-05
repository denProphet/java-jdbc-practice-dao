package entities;

public class Author {

    public int id;
    public String name;
    public int yearOfBirth;

    public Author(String name, int yearOfBirth) {

        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public Author() {
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
