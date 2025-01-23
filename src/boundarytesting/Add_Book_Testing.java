package boundarytesting;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import useraccess.Manager;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


public class Add_Book_Testing {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException,
            IOException, ClassNotFoundException {

        manager = new Manager();
    }

    @ParameterizedTest
    @CsvSource({
            // 6 characters ISBN(lower bound)
            "111aaa, Albert Kamy, I huaji, Roman, 23/02/2024, 10, 15, 4",
            // 10 characters ISBN(in between bounds)
            "1222a35io7, Fyodor Dostoyevski, Vellezerit Karamazov, Roman, 28/12/2024, 20, 30, 20",
            // 13 characters ISBN(lower bound)
            "1222a357u90pf,Shekspiri,Romeo dhe Zhulieta,Drame,28/12/2024,20,30,20"
    })
    public void testForValidISBN(String ISBN, String author, String title,
                                 String genre, String publishDate,
                                 int purchased_price, int selling_price, int stock)
            throws ISBNnotValidException, IOException {
        Book book = new Book(ISBN, author, title, genre, publishDate,purchased_price,
                selling_price, stock);
        assertDoesNotThrow(() -> manager.addBooks(book));
    }


    @ParameterizedTest
    @CsvSource({
            "1234t, ISBN should be at least 6 characters long and no longer than 13 characters long", // Too short, lower bound violation
            "1234t44oplii0w, ISBN should be at least 6 characters long and no longer than 13 characters long" // Too long, upper bound violation
    })
    public void testForInvalidISBN(String isbn, String expectedMessage) throws
            IOException, ISBNnotValidException {

        Book book = new Book(isbn, "Albert Kamy", "The stranger",
                "Roman", "28/12/2024",
                20, 30, 20);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class, () -> manager.addBooks(book));
        assertEquals(expectedMessage, exception.getMessage());
    }


    @ParameterizedTest
    @CsvSource({
            "1234tp, Albert Kamy, The stranger, Roman, 28/12/2024, 20, 30, 20", // valid date, within bounds
            "1234tp, Albert Kamy, The stranger, Roman, 1/12/2024, 20, 30, 20",  // valid date, lower bound
            "1234tp, Albert Kamy, The stranger, Roman, 31/12/2024, 20, 30, 20"  // valid date, upper bound
    })
    public void test_for_valid_dateformat(String isbn, String author, String title, String genre,
                                          String publishDate, int quantity, int price, int pages)
            throws ISBNnotValidException, IOException {

        Book book = new Book(isbn, author, title, genre, publishDate, quantity, price, pages);

        assertDoesNotThrow(() -> manager.addBooks(book));
    }

    @ParameterizedTest
    @CsvSource({
            "28-12/2024, Invalid date format. The format required for the date to be entered is dd/MM/yyyy", // wrong separators
            "32/12/2024, Invalid date format. The format required for the date to be entered is dd/MM/yyyy", // days out of range
            "0/12/2024, Invalid date format. The format required for the date to be entered is dd/MM/yyyy"  // days less than minimum
    })
    public void test_for_invalid_dateformat(String publishDate, String expectedMessage) throws ISBNnotValidException, IOException {
        Book book = new Book(
                "1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                publishDate,
                20,
                30,
                20);

        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.addBooks(book));

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void test_for_valid_stock() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                1);
        assertDoesNotThrow(() -> manager.addBooks(book)); // stock is positive
    }

    @Test
    public void test_for_invalid_stock() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                0);
        IOException exception = assertThrows(IOException.class,
                () -> manager.addBooks(book));// stock is less
        // then lower bound

        assertEquals("Stock cannot be less than one when you add" +
                "a book", exception.getMessage());
    }


    @Test
    public void test_for_equal_ISBNs() throws ISBNnotValidException,
            IOException, DateNotValidException, BookNotFoundException,
            ClassNotFoundException {

        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                5);
        Book book2 = new Book("1234tp",
                "Dostoyevksi",
                "Crime and punishment",
                "Roman",
                "28/12/2024",
                20,
                40,
                9);

        manager.addBooks(book);

        ISBNnotValidException exception = assertThrows(
                ISBNnotValidException.class,
                ( () -> manager.addBooks(book2)));

        assertEquals("ISBN should be unique for different books,so " +
                        "if you\n" +
                "want to use ISBN =" + " " + book.getISBN() + " " + "," +
                        "you can use it for the book of title" +
                " " + book.getTitle() + " " + "written by" + " " +
                        book.getAuthor(),
                exception.getMessage());
    }
}
