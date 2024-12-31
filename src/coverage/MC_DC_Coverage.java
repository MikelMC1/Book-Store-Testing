package coverage;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Manager;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class MC_DC_Coverage {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException, ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_isbnValid() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        // Valid ISBN (6-13 characters)
        Book validBook = new Book("111111", "Victor Hugo",
                "Les Misérables", "Novel",
                "01/01/2025", 10, 25, 5);
        manager.addBooks(validBook);

        assertTrue(manager.getBooks().contains(validBook));
    }

    @Test
    public void test_isbnTooShort() throws ISBNnotValidException, IOException {
        // ISBN shorter than 6 characters
        Book shortISBNBook = new Book("12345", "Migjeni", "Vargje të Lira", "Poetry", "01/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(shortISBNBook));

        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }

    @Test
    public void test_isbnTooLong() throws ISBNnotValidException, IOException {
        // ISBN longer than 13 characters
        Book longISBNBook = new Book("12345678901234", "Franz Kafka", "The Trial", "Novel", "01/01/2025", 10, 25, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(longISBNBook));

        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }

    @Test
    public void test_sameISBNDifferentAuthorAndTitle() throws IOException,
            ISBNnotValidException, DateNotValidException,
            BookNotFoundException, ClassNotFoundException {
        // Adding a book with the same ISBN but different author or title
        Book firstBook = new Book("222222", "Victor Hugo", "Les Misérables", "Novel", "01/01/2025", 10, 25, 5);
        manager.addBooks(firstBook);

        Book conflictingBook = new Book("222222", "Migjeni", "Vargje të Lira", "Poetry", "02/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(conflictingBook));

        assertTrue(exception.getMessage().contains("ISBN should be unique for different books"));
    }

    @Test
    public void test_sameISBNSameAuthorAndTitle() throws IOException,
            ISBNnotValidException, DateNotValidException,
            BookNotFoundException, ClassNotFoundException {

        // Adding a book with the same ISBN, author, and title
        Book firstBook = new Book("333334", "Franz Kafka",
                "Metamorfoza", "Novel", "01/01/2025",
                10, 25, 5);
        manager.addBooks(firstBook);

        Book duplicateBook = new Book("333334", "Franz Kafka",
                "Metamorfoza", "Novel", "01/01/2025",
                15, 30, 10);
        manager.addBooks(duplicateBook);

        for(Book book : manager.getBooks()) {
            if (book.getISBN().equals(duplicateBook.getISBN())) {
                assertEquals(15,book.getStock());
            }
        }
    }

    @Test
    public void test_invalidStock() throws ISBNnotValidException, IOException {
        // Invalid stock (less than 1)
        Book invalidStockBook = new Book("444444", "Migjeni", "Vargje të Lira", "Poetry", "01/01/2025", 10, 20, 0);

        IOException exception = assertThrows(IOException.class,
                () -> manager.addBooks(invalidStockBook));

        assertEquals("Stock cannot be less than one when you adda book", exception.getMessage());
    }

    @Test
    public void test_validStock() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        // Valid stock (greater than or equal to 1)
        Book validStockBook = new Book("555555", "Victor Hugo", "Notre-Dame de Paris", "Novel", "01/01/2025", 10, 30, 5);

        manager.addBooks(validStockBook);

        assertTrue(manager.getBooks().contains(validStockBook));
    }
}
