package models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private String isbn;
    private String publisher;
    private int quantity;
    private int available;

    public Book(int id, String title, String author, String genre, int year, String isbn, String publisher, int quantity, int available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.isbn = isbn;
        this.publisher = publisher;
        this.quantity = quantity;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
