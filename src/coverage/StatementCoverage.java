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


public class StatementCoverage {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_for_invalid_ISBN() throws ISBNnotValidException, IOException {
        Book book = new Book("1234","Ismail Kadare","Pallati i Endrrave",
                "Roman","1/01/2025",12,
                20,10);
        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(book));
        assertEquals("ISBN should be at least 6 " +
                     "characters long", exception.getMessage());
    }

    @Test
    public void test_for_invalid_stock() throws ISBNnotValidException, IOException {
        Book book = new Book("123456","Ismail Kadare","Pallati i Endrrave",
                "Roman","1/01/2025",12,
                20,0);
        IOException exception = assertThrows(IOException.class,
                () -> manager.addBooks(book));
        assertEquals("Stock cannot be less than one when you " +
                     "add" + "a book", exception.getMessage());
    }

    @Test
    public void test_for_valid_book() throws ISBNnotValidException, IOException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {

        assertDoesNotThrow(() -> manager.Read_books());
        Book book = new Book("89238rtyeq","Balzak",
                "Xha Gorioi", "Roman","7/01/2025",
                12, 20,10);
        manager.addBooks(book);
        assertDoesNotThrow(() -> manager.isValid(book.getPurchased_date()));
        assertTrue(manager.getBooks().contains(book));
        assertDoesNotThrow(() -> manager.writeBooksToFile());


    }

    @Test
    public void test_for_valid_book2() throws ISBNnotValidException, IOException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {

        // Add an initial book to the list
        Book book1 = new Book("123458", "Shekspiri", "Rome dhe Zhulieta",
                "Drame", "3/01/2025", 12,
                20, 10);
        manager.addBooks(book1);

        // Add another book with the same ISBN and check if the stock is updated
        Book book2 = new Book("123458", "Shekspiri", "Rome dhe Zhulieta",
                "Drame", "3/01/2025", 12,
                20, 5);
        manager.addBooks(book2);

        // Retrieve the updated book from the list
        for (Book book : manager.getBooks()) {
            if (book.getISBN().equals("123458")) {
                assertEquals(15, book.getStock());
                return;
            }
        }

    }

    @Test
    public void test_for_invalid_book() throws ISBNnotValidException, IOException {

        // when a book with same ISBN is added, and the stock is being updated
        Book book = new Book("123456","Shekspiri",
                "Hamleti", "Drame",
                "3/01/2025",12,
                20,5);
        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(book));
        assertEquals("ISBN should be unique for " +
                     "different books,so if you\n" +
                     "want to use ISBN =" + " " + "123456" + " " + "," +
                     "you can use it for the book of title" +
                     " " + "Pallati i Endrrave" + " " + "written by" + " " +
                     "Ismail Kadare", exception.getMessage());
    }

    @Test
    public void test_for_invalid_date() throws ISBNnotValidException, IOException {

        // for valid dates we have tested in the above methods
        Book book = new Book("1299990","Shekspiri",
                "Hamleti", "Drame",
                "3-01/2025",12,
                20,5);
        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.addBooks(book));
        assertEquals("Invalid date format. " +
                     "The format required for the date to be entered is"
                     + " " + "dd/MM/yyyy", exception.getMessage());
    }




}
