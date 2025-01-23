package classevaluation2;
import book.Book;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Evalation {
    private static Librarian librarian;

    @BeforeAll
    public static void setUp() throws IOException, ClassNotFoundException {
        // Initialize the librarian before all tests
        librarian = new Librarian();
    }

    @BeforeEach
    public void resetState() {
        // Reset the book list before each test
        librarian.getBooks().clear();
    }

    @Test
    public void shouldAddFirstBookSuccessfully() throws ISBNnotValidException, IOException {
        // Create a new Book object with valid details
        Book book = new Book(
                "12345",            // ISBN
                "Test Author",      // Author
                "Test Book",        // Title
                "Fiction",          // Category
                "1/01/2027",        // Purchased Date
                20.0,               // Purchase Price
                25.0,               // Selling Price
                10                  // Stock
        );

        // Add the book to the librarian's list
        librarian.addBookstolist(book);

        // Evaluate internal state (the book should be added to the list)
        assertEquals(1, librarian.getBooks().size(), "Book should be added to the list.");
        assertTrue(librarian.getBooks().contains(book), "The added book should be in the list.");
    }


    @Test
    public void shouldAddBooksToFullList() throws ISBNnotValidException, IOException {
        // Add 1000 books to the list
        for (int i = 0; i < 1000; i++) {
            librarian.addBookstolist(new Book(
                    String.valueOf(i),       // ISBN
                    "Author " + i,           // Author
                    "Book " + i,             // Title
                    "Category",              // Category
                    "1/01/2027",            // Purchased Date
                    20.0,                    // Purchase Price
                    30.0,                    // Selling Price
                    10                       // Stock
            ));
        }

        // Assert the list's size (state validation)
        assertEquals(1000, librarian.getBooks().size(), "The list should contain 1000 books.");
    }

    @Test
    public void shouldHandleEmptyBookList() {
        // The list should be empty before adding any books

        assertTrue(librarian.getBooks().isEmpty(), "The list should be empty initially.");
    }

    @Test
    public void shouldAllowAddingMultipleBooks() throws ISBNnotValidException, IOException {
        // Add multiple books to the list
        Book book1 = new Book("12345", "Author 1", "Book 1", "Fiction", "1/01/2024", 20.0, 25.0, 10);
        Book book2 = new Book("67890", "Author 2", "Book 2", "Non-Fiction", "2/02/2024", 22.0, 28.0, 15);

        librarian.addBookstolist(book1);
        librarian.addBookstolist(book2);

        // Assert that the list contains both books
        assertEquals(2, librarian.getBooks().size(), "The list should contain 2 books.");
    }

    @Test
    public void shouldNotAllowNullBook() {
        // Try to add a null book to the list
        assertThrows(NullPointerException.class, () -> librarian.addBookstolist(null));
    }

    // Additional class evaluation test for internal state
    @Test
    public void shouldMaintainCorrectBookProperties() throws ISBNnotValidException, IOException {
        Book book = new Book("12345", "Test Author", "Test Book", "Fiction", "1/01/2024", 20.0, 25.0, 10);
        librarian.addBookstolist(book);

        // Ensure internal properties of the book are correct
        Book addedBook = librarian.getBooks().get(0);
        assertNotNull(addedBook, "The book should be added to the list.");
        assertEquals("12345", addedBook.getISBN(), "The ISBN should be correct.");
        assertEquals("Test Author", addedBook.getAuthor(), "The author should be correct.");
        assertEquals(20.0, addedBook.getPurchased_price(), "The purchase price should be correct.");
        assertEquals(25.0, addedBook.getSelling_price(), "The selling price should be correct.");
    }
}


