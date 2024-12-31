package book;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import exceptions.ISBNnotValidException;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class Book implements Serializable {
    private String ISBN;
    private String author;
    private String title;
    private String book_category;
    private String purchased_date;
    private double purchased_price;
    private double selling_price;
    private int stock;

    @Serial
    private static final long serialVersionUID = 1L;

    public Book(String ISBN, String author, String title,
                String book_category,
               String purchased_date, double purchased_price,
                double selling_price, int stock) throws ISBNnotValidException,
            IOException {

        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.book_category = book_category;
        this.purchased_date = purchased_date;
        this.purchased_price = purchased_price;
        this.selling_price = selling_price;
        this.stock = stock;
    }

    public Book() {
    }

    public String getISBN() {
        return this.ISBN;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBook_category() {
        return this.book_category;
    }

    public String getPurchased_date() {
        return this.purchased_date;
    }

    public double getPurchased_price() {
        return this.purchased_price;
    }

    public double getSelling_price() {
        return this.selling_price;
    }

    public int getStock() {
        return this.stock;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    public void setPurchased_date(String purchased_date) {
        this.purchased_date = purchased_date;
    }

    public void setPurchased_price(double purchased_price) {
        this.purchased_price = purchased_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("ISBN=%s author=%s title=%s book_category=%s purchased_date=%s purchased_price=%.1f stock=%d",
                this.ISBN, this.author, this.title, this.book_category,
                this.purchased_date, this.purchased_price, this.stock);
    }
}
