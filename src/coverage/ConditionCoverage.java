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

public class ConditionCoverage {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException, ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_invalidISBNLengthLessThan6() throws IOException, ISBNnotValidException {
        Book shortISBNBook = new Book("12345", "Jean-Paul Sartre", "Nausea",
                "Philosophy", "01/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(shortISBNBook));

        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }

    @Test
    public void test_invalidISBNLengthGreaterThan13() throws IOException, ISBNnotValidException {
        Book longISBNBook = new Book("12345678901234", "Naim Frashëri", "Bagëti e Bujqësi",
                "Poetry", "01/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(longISBNBook));

        assertEquals("ISBN should be at least 6 characters long",
                exception.getMessage());
    }

    @Test
    public void test_validISBNLength() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {

        Book validISBNBook = new Book("1234567", "Homer",
                "The Iliad", "Epic",
                "01/01/2025", 10, 20, 5);
        // also valid stock and unique ISBN which means the condition
        // if (book.getISBN().equals(book1.getISBN())) is taken false

        manager.addBooks(validISBNBook);

        assertTrue(manager.getBooks().contains(validISBNBook));
    }

    @Test
    public void test_existingBookSameAuthorAndTitle() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {

        // if (book.getISBN().equals(book1.getISBN())) is taken true

        Book existingBook = new Book("1234567890123", "Homer",
                "The Odyssey", "Epic",
                "01/01/2025", 10, 20, 5);
        manager.addBooks(existingBook);

        Book duplicateBook = new Book("1234567890123", "Homer", "The Odyssey",
                "Epic", "02/01/2025",
                10, 20, 5);
        manager.addBooks(duplicateBook);

        for (Book book : manager.getBooks()) {
            if (book.getISBN().equals("1234567890123")) {
                assertEquals(30, book.getStock());
            }
        }
    }

    @Test
    public void test_invalidStock() throws IOException, ISBNnotValidException {
        Book invalidStockBook = new Book("1234567890123", "Naim Frashëri", "Lulet e Verës",
                "Poetry", "01/01/2025",
                10, 20, 0);

        IOException exception = assertThrows(IOException.class,
                () -> manager.addBooks(invalidStockBook));

        assertEquals("Stock cannot be less than one when you adda book",
                exception.getMessage());
    }

    @Test
    public void test_validStock() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        Book validStockBook = new Book("123456789012l", "Fjodor Dostojevksi",
                "Idiot",
                "Roman", "1/01/2025",
                10, 20, 5);

        manager.addBooks(validStockBook);

        assertTrue(manager.getBooks().contains(validStockBook));
    }

    @Test
    public void test_authorMatchesButTitleDiffers() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        Book existingBook = new Book("12345678901ph", "Homer", "The Iliad",
                "Epic", "01/01/2025", 10, 20, 5);
        manager.addBooks(existingBook);

        Book newBook = new Book("12345678901ph", "Homer", "The Odyssey",
                "Epic", "02/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(newBook));

        assertTrue(exception.getMessage().contains("ISBN should be unique for different books"));
    }

    @Test
    public void test_titleMatchesButAuthorDiffers() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        Book existingBook = new Book("12345678901mb", "Naim Frashëri", "Bagëti e Bujqësi",
                "Poetry", "01/01/2025", 10, 20, 5);
        manager.addBooks(existingBook);

        Book newBook = new Book("12345678901mb", "Jean-Paul Sartre",
                "Bagëti e Bujqësi",
                "Philosophy", "02/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(newBook));

        assertTrue(exception.getMessage().contains("ISBN should be unique for different books"));
    }
}
