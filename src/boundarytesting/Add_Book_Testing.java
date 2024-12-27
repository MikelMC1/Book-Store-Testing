package boundarytesting;

import book.Book;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    public void test_for_valid_ISBN() throws ISBNnotValidException, IOException {
        Book book = new Book("1222a3",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                20); //6 characters, lower bound
        assertDoesNotThrow(() -> manager.addBooks(book));
    }

    @Test
    public void test_for_valid_ISBN2() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1222a35io7",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                20); //10 characters, between boundaries
        assertDoesNotThrow( () -> manager.addBooks(book));
    }

    @Test
    public void test_for_valid_ISBN3() throws ISBNnotValidException, IOException {
        Book book = new Book("1222a357u90pf",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                20); //13 characters, upper bound
        assertDoesNotThrow(() -> manager.addBooks(book));
    }

    @Test
    public void test_for_invalid_ISBN() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234t",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                20); // invalid ISBN little bit than lower bound
                           // (out of range)
       ISBNnotValidException exception =
               assertThrows(ISBNnotValidException.class, () ->
                       manager.addBooks(book));
       assertEquals("ISBN should be at least 6 characters long",
               exception.getMessage());
    }

    @Test
    public void test_for_invalid_ISBN2() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234t44oplii0w",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                20); // invalid ISBN bigger than upper bound
        // (out of range)
        ISBNnotValidException exception =
                assertThrows(ISBNnotValidException.class, () ->
                        manager.addBooks(book));
        assertEquals("ISBN should be at least 6 characters long",
                exception.getMessage());
    }

    @Test
    public void test_for_valid_dateformat() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28/12/2024",
                20,
                30,
                20); // valid date format dd/MM/yy -> 28/12/2024
                          //also within range of low and upper bound
        assertDoesNotThrow(() -> manager.addBooks(book));
    }

    @Test
    public void test_for_valid_dateformat2() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "1/12/2024",
                20,
                30,
                20); // valid date format as it is in lower bound
        assertDoesNotThrow(() -> manager.addBooks(book));
    }

    @Test
    public void test_for_valid_dateformat3() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "31/12/2024",
                20,
                30,
                20); // valid date format as it is in upper bound
        assertDoesNotThrow(() -> manager.addBooks(book));
    }

    @Test
    public void test_for_invalid_dateformat() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28-12/2024",
                20,
                30,
                20); //invalid date format 28-12/2024
       DateNotValidException exception = assertThrows(DateNotValidException.class,
               () -> manager.addBooks(book));
       assertEquals("Invalid date format. " +
               "The format required for the date to be entered is"
               + " " + "dd/MM/yyyy",exception.getMessage());
    }

    @Test
    public void test_for_invalid_dateformat2() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "32/12/2024",
                20,
                30,
                20); //invalid date as it is bigger than max number
                           // of days (upper bound)
        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.addBooks(book));
        assertEquals("Invalid date format. " +
                "The format required for the date to be entered is"
                + " " + "dd/MM/yyyy",exception.getMessage());
    }

    @Test
    public void test_for_invalid_dateformat3() throws ISBNnotValidException,
            IOException {
        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "0/12/2024",
                20,
                30,
                20); //invalid date as it is less than min number
        // of days (lower bound)
        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.addBooks(book));
        assertEquals("Invalid date format. " +
                "The format required for the date to be entered is"
                + " " + "dd/MM/yyyy",exception.getMessage());
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
            IOException, DateNotValidException, BookNotFoundException, ClassNotFoundException {

        Book book = new Book("1234tp",
                "Albert Kamy",
                "The stranger",
                "Roman",
                "28-12/2024",
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

        assertEquals("ISBN should be unique for different books,so if you\n" +
                "want to use ISBN =" + " " + book2.getISBN() + " " + "," + "you can use it for the book of title" +
                " " + book2.getTitle() + " " + "written by" + " " + book2.getAuthor(),
                exception.getMessage());
    }
}
