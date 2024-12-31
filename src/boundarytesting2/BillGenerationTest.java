package boundarytesting2;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BillGenerationTest {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException {
        librarian = new Librarian();
    }



    @Test
    public void testOutOfStock() {
        try {
            librarian.addBookstolist(new Book("12345", "Test Author", "Test Book", "Fiction", "01/01/2024", 10.0, 20.0, 0));
            assertThrows(BookNotFoundException.class, () -> librarian.Bill("01/01/2024", "Test Book", 1));
        } catch (Exception e) {
            fail("Out of stock should throw BookNotFoundException");
        }
    }

    @Test
    public void testNotEnoughStock() {
        try {
            Book book = new Book("12345", "Test Author", "Test Book", "Fiction", "01/01/2024", 10.0, 20.0, 5);
            librarian.addBookstolist(book);
            assertThrows(BookNotFoundException.class, () -> librarian.Bill("01/01/2024", "Test Book", 10));
        } catch (Exception e) {
            fail("Not enough stock should throw BookNotFoundException");
        }
    }

    @Test
    public void testBookNotAvailable() {
        try {
            assertThrows(BookNotFoundException.class, () -> librarian.Bill("01/01/2024", "Nonexistent Book", 1));
        } catch (Exception e) {
            fail("Nonexistent book should throw BookNotFoundException");
        }
    }
    @Test
    public void testInvalidDateFormat() {
        try {
            // Adding a book with sufficient stock
            Book book = new Book("12345", "Test Author", "Test Book", "Fiction", "01/01/2024", 10.0, 20.0, 10);
            librarian.addBookstolist(book);

            // Attempt to generate a bill with an invalid date format
            assertThrows(DateNotValidException.class, () -> {
                librarian.Bill("2024-01-01", "Test Book", 1); // Invalid date format should throw exception
            });
        } catch (Exception e) {
            fail("Invalid date format should throw IllegalArgumentException");
        }
    }


    @Test
    public void testLargeQuantity() {
        try {
            // Adding a book with limited stock
            Book book = new Book("12345", "Test Author", "Test Book", "Fiction", "01/01/2024", 10.0, 20.0, 10);
            librarian.addBookstolist(book);

            // Attempt to generate a bill with a quantity larger than stock
            assertThrows(BookNotFoundException.class, () -> {
                librarian.Bill("01/01/2024", "Test Book", 100); // Exceeding stock should throw exception
            });
        } catch (Exception e) {
            fail("Requesting a quantity larger than stock should throw BookNotFoundException");
        }
    }





}


