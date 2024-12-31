package coveragei;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class statementCoveragei {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.librarian = new Librarian(); // Using Librarian instead of Manager
    }

    // Statement coverage for testlargequantity() method
    @Test
    public void testlargequantity() throws ISBNnotValidException, IOException {
        // Create a book with a large stock quantity
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Large stock quantity

        // Add the book to the librarian's list
        librarian.addBookstolist(book); // Changed from manager.addBooks(book)

        // Assert that the book has been added
        assertTrue(librarian.getBooks().contains(book)); // Changed from manager.getBooks()

        // Check if the stock value is as expected
        assertEquals(100000, book.getStock());

        // Check if the book data can still be written to a file
        assertDoesNotThrow(() -> librarian.writeBooksToFile()); // Changed from manager.writeBooksToFile()
    }

    // Edge case: When stock exceeds the allowed limit
    @Test
    public void testlargequantity_exceeds_limit() throws ISBNnotValidException, IOException {
        // Create a book with an overly large stock quantity (exceeding the limit)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 1000000); // Exceeds the stock limit

        // Ensure an exception is thrown when adding a book with stock beyond the limit
        IOException exception = assertThrows(IOException.class, () -> librarian.addBookstolist(book)); // Changed from manager.addBooks()
        assertEquals("Stock exceeds the allowed limit", exception.getMessage());
    }

    // Edge case: When stock is zero (a valid boundary case to test)
    @Test
    public void testlargequantity_zero_stock() throws ISBNnotValidException, IOException {
        // Create a book with zero stock quantity (boundary case)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 0); // Zero stock quantity

        // Add the book to the librarian's list
        librarian.addBookstolist(book); // Changed from manager.addBooks()

        // Ensure that the book is still added to the list
        assertTrue(librarian.getBooks().contains(book)); // Changed from manager.getBooks()

        // Check if the stock is zero
        assertEquals(0, book.getStock());

        // Ensure the system can write the books to a file
        assertDoesNotThrow(() -> librarian.writeBooksToFile()); // Changed from manager.writeBooksToFile()
    }

    // Edge case: Large stock value but invalid book data
    @Test
    public void testlargequantity_invalid_ISBN() throws ISBNnotValidException, IOException {
        // Create a book with an invalid ISBN but large stock
        Book book = new Book("1234", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Invalid ISBN

        // Assert that an ISBNnotValidException is thrown when trying to add the book
        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class, () -> librarian.addBookstolist(book)); // Changed from manager.addBooks()
        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }
}
