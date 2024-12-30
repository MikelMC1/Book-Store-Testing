package boundarytesting2;

import book.Book;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddBookToListTest {

    private static Librarian librarian;

    @BeforeAll
    public static void setUp() throws IOException, ClassNotFoundException {
        librarian = new Librarian();
    }

    @BeforeEach
    public void resetState() {
        // Resetting the list before each test (e.g., clearing books)
        librarian.getBooks().clear();
    }

    @Test
    public void testAddFirstBook() throws ISBNnotValidException, IOException {
        // Create a new Book object using the correct constructor
        Book book = new Book(
                "12345",            // ISBN
                "Test Author",      // Author
                "Test Book",        // Title
                "Fiction",          // Book Category
                "01/01/2024",       // Purchased Date (assumed format dd/MM/yyyy)
                20.0,               // Purchased Price
                25.0,               // Selling Price
                10                  // Stock
        );
        librarian.addBookstolist(book);
        assertEquals(1, librarian.getBooks().size(), "Book should be added to the list");
    }

    @Test
    public void testAddBookToFullList() throws ISBNnotValidException, IOException {
        // Adding 1000 books to the librarian's book list
        for (int i = 0; i < 1000; i++) {
            librarian.addBookstolist(new Book(
                    String.valueOf(i),       // ISBN
                    "Author " + i,           // Author
                    "Book " + i,             // Title
                    "Category",              // Book Category
                    "01/01/2024",            // Purchased Date
                    20.0,                    // Purchased Price
                    30.0,                    // Selling Price
                    10                       // Stock
            ));
        }
        assertEquals(1000, librarian.getBooks().size(), "The list should contain 1000 books");
    }
}
