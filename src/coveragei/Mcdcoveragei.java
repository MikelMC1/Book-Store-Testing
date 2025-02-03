package coveragei;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian; // Changed from Manager to Librarian
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class Mcdcoveragei {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.librarian = new Librarian(); // Using Librarian instead of Manager
    }

    // MC/DC test for adding a book with large stock quantity
    @Test
    public void testlargequantity_mcdc() throws ISBNnotValidException, IOException {
        // Create a book with a valid ISBN and a large stock quantity
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Valid ISBN, large stock

        // Add the book to the librarian's list (this will test the condition branches)
        librarian.addBookstolist(book);

        // Test the conditions independently
        // Condition 1: Check if ISBN is valid
        assertTrue(librarian.getBooks().contains(book)); // Book is added if ISBN is valid

        // Condition 2: Stock quantity must be validated, so it should accept large stock
        assertEquals(100000, book.getStock());

        // Condition 3: Write the book to the file after validation
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // MC/DC test for adding a book with invalid ISBN (Condition 1 is false)
    @Test
    public void testInvalidISBN_mcdc() throws ISBNnotValidException, IOException {
        // Create a book with an invalid ISBN (less than 6 characters)
        Book book = new Book("1234", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100000); // Invalid ISBN

        // Assert that an exception is thrown due to invalid ISBN
        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class, () -> librarian.addBookstolist(book));
        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());

        // Condition 2 and 3 are not executed since the book was not added due to the invalid ISBN
    }

    // MC/DC test for adding a book with zero stock (Condition 2 is false)
    @Test
    public void testZeroStock_mcdc() throws ISBNnotValidException, IOException {
        // Create a book with zero stock quantity (valid ISBN)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 0); // Zero stock

        // Add the book to the librarian's list
        librarian.addBookstolist(book);

        // Assert that the book is added with zero stock
        assertTrue(librarian.getBooks().contains(book));
        assertEquals(0, book.getStock());  // Condition 2 is checked (zero stock)

        // Check if the book is successfully written to file (Condition 3)
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // MC/DC test for exceeding the stock limit (Condition 3 is false)
    @Test
    public void testExceedsStockLimit_mcdc() throws ISBNnotValidException, IOException {
        // Create a book with an excessively large stock quantity (exceeds limit)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 1000000); // Stock exceeds allowed limit

        // Assert that an exception is thrown due to exceeding the stock limit
        IOException exception = assertThrows(IOException.class, () -> librarian.addBookstolist(book));
        assertEquals("Stock exceeds the allowed limit", exception.getMessage());

        // Condition 1 and 2 are not executed since the book is rejected due to the stock limit
    }

    // MC/DC test for adding a book with valid stock and ISBN (Condition 1, 2, and 3 are true)
    @Test
    public void testValidBook_mcdc() throws ISBNnotValidException, IOException {
        // Create a book with valid ISBN and stock quantity
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "01/01/2025", 50, 1000, 100); // Valid ISBN and stock

        // Add the book to the librarian's list
        librarian.addBookstolist(book);

        // Assert that the book is successfully added
        assertTrue(librarian.getBooks().contains(book));
        assertEquals(100, book.getStock());  // Stock value is valid (Condition 2 is true)

        // Ensure the book is written to the file after being added (Condition 3 is true)
        assertDoesNotThrow(() -> librarian.writeBooksToFile());
    }

    // MC/DC test for adding a book with an invalid date (Condition 4)
    @Test
    public void testInvalidDate_mcdc() throws ISBNnotValidException, IOException {
        // Create a book with an invalid date format (Condition 4 should be false)
        Book book = new Book("987654", "Author Name", "Book Title",
                "Genre", "31-12-2025", 50, 1000, 100); // Invalid date format

        // Assert that an exception is thrown due to the invalid date format
        DateNotValidException exception = assertThrows(DateNotValidException.class, () -> librarian.addBookstolist(book));
        assertEquals("Invalid date format. The format required for the date to be entered is dd/MM/yyyy", exception.getMessage());
    }
}
