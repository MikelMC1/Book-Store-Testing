package coveragei;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionCoveragei {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.librarian = new Librarian(); // Using Librarian instead of Manager
    }

    // Condition test for adding a book with large stock quantity
    @Test
    public void testlargequantity_condition() throws ISBNnotValidException, IOException {
        // Create a book with a valid ISBN and a large stock quantity
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Valid ISBN, large stock

        // Add the book to the librarian's list (This will hit the conditions inside addBooksToList)
        librarian.addBookstolist(book);

        // Assert that the book is added to the librarian's collection (Condition 1: stock is valid)
        assertTrue(librarian.getBooks().contains(book));

        // Check if the stock value is correctly updated (Condition 2: stock is large)
        assertEquals(100000, book.getStock());

        // Ensure the system writes the books to a file (Condition 3: Write to file is possible)
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // Condition test for adding a book with invalid ISBN (Condition 1: Invalid ISBN)
    @Test
    public void testlargequantity_invalid_ISBN_condition() throws ISBNnotValidException, IOException {
        // Create a book with an invalid ISBN (less than 6 characters)
        Book book = new Book("1234", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Invalid ISBN (less than 6 characters)

        // Assert that an exception is thrown (Condition 1: ISBN validation failure)
        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class, () -> librarian.addBookstolist(book));
        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }

    // Condition test for adding a book with zero stock (Condition 2: Stock is zero)
    @Test
    public void testlargequantity_zero_stock_condition() throws ISBNnotValidException, IOException {
        // Create a book with zero stock quantity (valid ISBN)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 0); // Zero stock (Condition 2: Check for zero stock)

        // Add the book to the librarian's list
        librarian.addBookstolist(book);

        // Assert that the book is added with zero stock (Condition 2: Zero stock)
        assertTrue(librarian.getBooks().contains(book));
        assertEquals(0, book.getStock());  // Validate stock is zero

        // Ensure the system writes the books to a file
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // Condition test for exceeding the stock limit (Condition 3: Stock exceeds allowed limit)
    @Test
    public void testlargequantity_exceeds_limit_condition() throws ISBNnotValidException, IOException {
        // Create a book with an overly large stock quantity (exceeds limit)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 1000000); // Stock exceeds allowed limit

        // Assert that an exception is thrown (Condition 3: Exceeds stock limit)
        IOException exception = assertThrows(IOException.class, () -> librarian.addBookstolist(book));
        assertEquals("Stock exceeds the allowed limit", exception.getMessage());
    }

    // Condition test for adding a book with valid stock and ISBN
    @Test
    public void testlargequantity_valid_stock_condition() throws ISBNnotValidException, IOException {
        // Create a book with valid stock and ISBN
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100); // Valid stock and ISBN

        // Add the book to the librarian's list
        librarian.addBookstolist(book);

        // Assert that the book is added correctly
        assertTrue(librarian.getBooks().contains(book));
        assertEquals(100, book.getStock());  // Validate stock is correct

        // Ensure the system writes the books to a file
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }
}
