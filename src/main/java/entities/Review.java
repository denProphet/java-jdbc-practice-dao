package entities;

public class Review {

   public int id;
   public int userId;
   public String text;

    public Review(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public Review() {
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                '}';
    }
}
