package systemtesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import book.Book;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import org.junit.Before;
import useraccess.Manager;
import org.junit.Test;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import useraccess.Librarian;

public class BookSystemTesting {

    private Librarian librarian;
    private Manager manager;

    @Before
    public void setUp() throws IOException, ClassNotFoundException,
            BookNotFoundException {
        this.librarian = new Librarian();
        this.manager = new Manager();
    }

    @Test
    public void testAddAndRetrieveBooks() throws IOException, ClassNotFoundException,
            ISBNnotValidException, DateNotValidException, BookNotFoundException,
            ParseException {

        Book book1 = new Book("12345k", "Shekspiri", "Romeo dhe Zhulieta",
                "Drame",
                "01/01/2026", 10, 20,7);
        Book book2 = new Book("12345tui", "Pushkin", "Ciganet",
                "Poem", "01/01/2026",
                10, 20,7);

        // Act
        manager.addBooks(book1);
        manager.addBooks(book2);
        ArrayList<Book> books = librarian.getBooks();

        setUp();

        // Assert
        assertEquals(8, books.size());
        assertEquals("I huaji", books.get(0).getTitle());
        assertEquals("Krim dhe ndeshkim", books.get(1).getTitle());

        ArrayList<Book> actual_value = this.manager.
                Statistics_of_BooksBought_without_Filters();
        String expected = """
     [ISBN=111aaa author=Albert Kamy title=I huaji book_category=Roman\s
     purchased_date=23/02/2024 purchased_price=10.0 stock=4, ISBN=144ty58\s
     author=Fjodor Dostojevski title=Krim dhe ndeshkim book_category=Roman\s
     purchased_date=03/03/2024 purchased_price=10.0 stock=13, ISBN=70000000\s
     author=Miguel De Servantes title=Don Kishoti book_category=Roman\s
     purchased_date=03/03/2024 purchased_price=20.0 stock=17, ISBN=9001101\s
     author=Leon Tolstoi title=Ana Karenina book_category=Roman\s
     purchased_date=13/03/2024 purchased_price=13.0 stock=35, ISBN=134367yu\s
     author=Dritero Agolli title=Shkelqimi dhe Renia e shokut Zylo book_category=Roman\s
     purchased_date=16/03/2024 purchased_price=10.0 stock=44, ISBN=18211821\s
     author=Shekspiri title=Hamleti book_category=Drame purchased_date=16/03/2024\s
     purchased_price=10.0 stock=15, ISBN=12345k author=Shekspiri title=Romeo dhe Zhulieta\s
     book_category=Drame purchased_date=01/01/2026 purchased_price=10.0 stock=7,\s
     ISBN=12345tui author=Pushkin title=Ciganet book_category=Poem\s
     purchased_date=01/01/2026 purchased_price=10.0 stock=7]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);

        String start_date = "25/02/2024";
        String end_date = "10/03/2024";
        // date in between where books are bought but no lower bound or upper
        // bound included

        ArrayList<Book> actual_value2 = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected2 = """
        [ISBN=144ty58 author=Fjodor Dostojevski title=Krim dhe ndeshkim\s
        book_category=Roman purchased_date=03/03/2024 purchased_price=10.0 stock=13,\s
        ISBN=70000000 author=Miguel De Servantes title=Don Kishoti book_category=Roman\s
        purchased_date=03/03/2024 purchased_price=20.0 stock=17]""";
        expected2 = expected2.trim().replaceAll("\\s+", " ");
        String actual2 = actual_value2.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected2, actual2);

        String date = "23/02/2024"; // first date in file(lower bound)

        ArrayList<Book> actual_value3 = this.manager.
                Daily_Statistics_ofBooksBought(date);
        String expected3 = """
               [ISBN=111aaa author=Albert Kamy title=I huaji book_category=Roman\s
               purchased_date=23/02/2024 purchased_price=10.0 stock=4]""";
        expected3 = expected3.trim().replaceAll("\\s+", " ");
        String actual3 = actual_value3.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected3,actual3);

    }

    @Test
    public void testSalesAndRetrieveSales() throws DateNotValidException,
            BookNotFoundException, ParseException, IOException, BillNotFoundException {

        String bill = librarian.Bill("20/04/2025","Ciganet",1);
       assertEquals("20/04/2025,12345tui,Ciganet,1,20.0",bill);

       String actual_value = manager.getBooksSoldWithoutFilters();
        assertEquals("""
                2/03/2024, 144ty58, Krim dhe ndeshkim, 2, 60.0
                2/03/2024, 144ty58, Krim dhe ndeshkim, 2, 60.0
                2/03/2024, 144ty58, Krim dhe ndeshkim, 2, 60.0
                2/03/2024, 111aaa, I huaji, 1, 15.0
                8/03/2024, 70000000, Don Kishoti, 2, 50.0
                8/03/2024, 144ty58, Krim dhe ndeshkim, 1, 30.0
                14/03/2024, 111aaa, I huaji, 3, 45.0
                16/04/2024, 18211821, Hamleti, 2, 30.0
                16/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 1, 20.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 2, 40.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 1, 20.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 1, 20.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 1, 20.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 7, 140.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 7, 140.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 14, 280.0
                17/04/2024, 18211821, Hamleti, 2, 30.0
                17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 16, 320.0
                17/04/2024, 18211821, Hamleti, 2, 30.0
                20/04/2025, 12345tui, Ciganet, 1, 20.0""", actual_value);


        String start_date = "1/03/2024";
        String end_date = "15/03/2024";
        // dates outside bounds where books are sold but no lower bound
        // or upper bound included


        String actual_value2 = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("""
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, I huaji, 1 book/s
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s
                14/03/2024, I huaji, 3 book/s""", actual_value2);
    }

}
