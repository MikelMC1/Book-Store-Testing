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


public class BranchCoverage {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_addNewBook() throws ISBNnotValidException, IOException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        // Add a completely new book
        Book book = new Book("9780141182803", "George Orwell",
                "1984", "Dystopian",
                "01/01/2025", 15, 30, 5);

        manager.addBooks(book);

        // Verify the book was added
        assertTrue(manager.getBooks().contains(book));
    }

    @Test
    public void test_updateExistingBookStock() throws ISBNnotValidException, IOException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException {
        // Add an existing book
        Book existingBook = new Book("9780743273565", "F. Scott Fitzgerald",
                "The Great Gatsby", "Novel", "01/01/2025", 10, 25, 10);
        manager.addBooks(existingBook);

        // Add another book with the same ISBN, title, and author
        Book duplicateBook = new Book("9780743273565", "F. Scott Fitzgerald",
                "The Great Gatsby", "Novel", "02/01/2025", 10, 25, 5);
        manager.addBooks(duplicateBook);

        // Verify the stock is updated
        for (Book book : manager.getBooks()) {
            if (book.getISBN().equals("9780743273565")) {
                assertEquals(15, book.getStock());
            }
        }
    }

    @Test
    public void test_duplicateISBNDifferentBook() throws ISBNnotValidException,
            IOException, DateNotValidException, BookNotFoundException,
            ClassNotFoundException {
        // Add an existing book
        Book existingBook = new Book("9780553293357", "J.R.R. Tolkien",
                "The Hobbit", "Fantasy", "01/01/2025", 10, 25, 10);
        manager.addBooks(existingBook);

        // Add another book with the same ISBN but different title or author
        Book conflictingBook = new Book("9780553293357", "J.K. Rowling",
                "Harry Potter and the Sorcerer's Stone", "Fantasy", "02/01/2025", 10, 25, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(conflictingBook));

        // Verify the exception message
        assertEquals("ISBN should be unique for different books,so if you\n" +
                     "want to use ISBN = 9780553293357 ,you can use it for the book of title The Hobbit written by J.R.R. Tolkien", exception.getMessage());
    }

    @Test
    public void test_invalidISBN() throws ISBNnotValidException, IOException {
        // Add a book with an invalid ISBN
        Book invalidISBNBook = new Book("123", "Mary Shelley", "Frankenstein",
                "Horror", "01/01/2025", 10, 20, 5);

        ISBNnotValidException exception = assertThrows(ISBNnotValidException.class,
                () -> manager.addBooks(invalidISBNBook));

        // Verify the exception message
        assertEquals("ISBN should be at least 6 characters long", exception.getMessage());
    }

    @Test
    public void test_invalidStock() throws ISBNnotValidException, IOException {
        // Add a book with invalid stock
        Book invalidStockBook = new Book("9780061120084", "Harper Lee", "To Kill a Mockingbird",
                "Fiction", "01/01/2025", 10, 20, 0);

        IOException exception = assertThrows(IOException.class,
                () -> manager.addBooks(invalidStockBook));

        // Verify the exception message
        assertEquals("Stock cannot be less than one when you adda book", exception.getMessage());
    }
}
