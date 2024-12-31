package coveragei;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BranchCoveragei {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.librarian = new Librarian(); // Using Librarian instead of Manager
    }

    // Branch test for the testlargequantity() method
    @Test
    public void testlargequantity() throws ISBNnotValidException, IOException {
        // Create a book with a large stock quantity
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Large stock quantity

        // Add the book to the librarian's list (True branch of addBooks)
        librarian.addBookstolist(book);

        // Assert that the book has been added (True branch of contains)
        assertTrue(librarian.getBooks().contains(book));

        // Check if the stock value is as expected (True branch of assertEquals)
        assertEquals(100000, book.getStock());

        // Check if the book data can still be written to a file (True branch of assertDoesNotThrow)
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // Branch test for when stock exceeds the allowed limit (False branch for exceeding limit condition)
    @Test
    public void testlargequantity_exceeds_limit() throws ISBNnotValidException, IOException {
        // Create a book with an overly large stock quantity (exceeding the limit)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 1000000); // Exceeds the stock limit

        // Ensure an exception is thrown when adding a book with stock beyond the limit
        IOException exception = assertThrows(IOException.class, () -> librarian.addBookstolist(book)); // False branch for exceeding limit
        assertEquals("Stock exceeds the allowed limit", exception.getMessage());
    }

    // Branch test for when stock is zero (boundary case for stock being zero)
    @Test
    public void testlargequantity_zero_stock() throws ISBNnotValidException, IOException {
        // Create a book with zero stock quantity (boundary case)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 0); // Zero stock quantity

        // Add the book to the librarian's list (True branch for addBooks with valid stock)
        librarian.addBookstolist(book);

        // Ensure that the book is still added to the list (True branch for contains)
        assertTrue(librarian.getBooks().contains(book));

        // Check if the stock is zero (True branch for assertEquals)
        assertEquals(0, book.getStock());

        // Ensure the system can write the books to a file (True branch for assertDoesNotThrow)
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // Branch test for invalid ISBN (False branch for ISBN validation condition)
    @Test
    public void testlargequantity_invalid_ISBN() throws ISBNnotValidException, IOException {
        // Create a book with an invalid ISBN but large stock
        Book book = new Book("1234", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Invalid ISBN

        // Assert that an ISBNnotValidException is thrown when trying to add the book (False branch for ISBN validation)
        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class, () -> librarian.addBookstolist(book)); // False branch for invalid ISBN
        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }

    // Branch test for valid stock (True branch for valid stock case)
    @Test
    public void testlargequantity_valid_stock() throws ISBNnotValidException, IOException {
        // Create a book with valid stock quantity
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100); // Valid stock quantity

        // Add the book to the librarian's list (True branch for addBooks with valid stock)
        librarian.addBookstolist(book);

        // Ensure that the book is added to the list (True branch for contains)
        assertTrue(librarian.getBooks().contains(book));

        // Ensure the stock is correct (True branch for assertEquals)
        assertEquals(100, book.getStock());

        // Ensure the book data can be written to file (True branch for assertDoesNotThrow)
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }
}
