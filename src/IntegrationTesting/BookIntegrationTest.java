package IntegrationTesting;

import static org.junit.jupiter.api.Assertions.*;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import book.Book;
import useraccess.Librarian;
import useraccess.Manager;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class BookIntegrationTest {

    private Librarian librarian;
    private Manager manager;

    @BeforeEach
    public void setup() throws BookNotFoundException, IOException, ClassNotFoundException {
        this.manager = new Manager();
        this.librarian = new Librarian();
    }

    @Test
    public void testBookAdditionAndSale() throws IOException, ISBNnotValidException,
            DateNotValidException, BookNotFoundException, ClassNotFoundException,
            ParseException, BillNotFoundException {

        // Add a book
        Book book2 = new Book("18211821", "Shekspiri", "Hamleti",
                "Drame", "16/03/2024", 10, 15.0,
                4);
        manager.addBooks(book2);


        // Create a bill
        String bill = librarian.Bill("16/04/2024", "Hamleti", 2);
        assertEquals("16/04/2024,18211821,Hamleti,2,30.0", bill);

        // Check the number of books sold for the given date
        String dailySales = manager.getDaily_Statistics_of_BooksSold("16/04/2024");
        assertTrue(dailySales.contains("2 book/s"));
    }

    @Test
    public void testBookAddition_And_MonthlyStatistics() throws ISBNnotValidException,
            IOException, DateNotValidException, BookNotFoundException,
            ParseException, BillNotFoundException {


        String bill = librarian.Bill("16/04/2024","Shkelqimi dhe Renia e shokut" + " " +
                "Zylo",1);
        assertEquals("16/04/2024,134367yu,Shkelqimi dhe Renia e shokut Zylo,1,20.0",
                bill);

        //upper bound included
        String monthlySales = manager.getMonthly_Statistics_of_BooksSold("8/03/2024",
                "16/04/2024");
        assertEquals("""
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s
                14/03/2024, I huaji, 3 book/s
                16/04/2024, Hamleti, 2 book/s
                16/04/2024, Shkelqimi dhe Renia e shokut Zylo, 1 book/s""",monthlySales);

    }

    @Test
    public void testBookAddition_And_MonthlyStatistics2() throws IOException,
            DateNotValidException, BookNotFoundException,
            ParseException, BillNotFoundException {


        String bill = librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                "Zylo",2);
        assertEquals("17/04/2024,134367yu,Shkelqimi dhe Renia e shokut Zylo,2,40.0",
                bill);

        //lower bound included
        String monthlySales = manager.getMonthly_Statistics_of_BooksSold("2/03/2024",
                "8/03/2024");
        assertEquals("""
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, I huaji, 1 book/s
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s""",monthlySales);

    }

    @Test
    public void testBookAddition_And_MonthlyStatistics3() throws IOException,
            DateNotValidException, BookNotFoundException,
            ParseException, BillNotFoundException, ISBNnotValidException,
            ClassNotFoundException {

        Book book = new Book("134367yu","Dritero Agolli",
                "Shkelqimi dhe Renia e shokut" + " " + "Zylo",
                "Roman","17/04/2024",10,
                20,7);

        manager.addBooks(book);

        String bill = librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                        "Zylo",1);
        assertEquals("17/04/2024,134367yu,Shkelqimi dhe Renia e shokut Zylo,1,20.0",
                bill);

        //outside bounds dates
        String monthlySales = manager.getMonthly_Statistics_of_BooksSold("20/02/2024",
                "1/03/2024");
        assertEquals("No books are sold from" + " " + "20/02/2024" + " " + "to" +
                " " + "1/03/2024",monthlySales);

    }

    @Test
    public void testBookAddition_And_MonthlyStatistics4() throws
            IOException, DateNotValidException, BookNotFoundException,
            ParseException, BillNotFoundException, ISBNnotValidException,
            ClassNotFoundException {

        Book book = new Book("134367yu","Dritero Agolli",
                "Shkelqimi dhe Renia e shokut" + " " + "Zylo",
                "Roman","17/04/2024",10,
                20,7);

        manager.addBooks(book);

        String bill = librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                        "Zylo",1);
        assertEquals("17/04/2024,134367yu,Shkelqimi dhe Renia e shokut Zylo,1,20.0",
                bill);


        String monthlySales = manager.getBooksSoldWithoutFilters();
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
                        17/04/2024, 134367yu, Shkelqimi dhe Renia e shokut Zylo, 1, 20.0""",
                monthlySales);

    }

    @Test
    public void testBookAddition_And_Monthly_BooksBought_Statistics() throws
            IOException, DateNotValidException, BookNotFoundException,
            ISBNnotValidException,
            ClassNotFoundException {

         Book book = new Book("134367yu","Dritero Agolli",
                "Shkelqimi dhe Renia e shokut" + " " + "Zylo",
                "Roman","16/03/2024",10,
                20,7);

        manager.addBooks(book);

        ArrayList<Book> actual_value = this.manager.
               Statistics_of_BooksBought_without_Filters();
        String expected = """
     [ISBN=111aaa author=Albert Kamy title=I huaji book_category=Roman 
     purchased_date=23/02/2024 purchased_price=10.0 stock=4, 
     ISBN=144ty58 author=Fjodor Dostojevski title=Krim dhe ndeshkim 
     book_category=Roman purchased_date=03/03/2024 purchased_price=10.0 stock=13, 
     ISBN=70000000 author=Miguel De Servantes title=Don Kishoti book_category=Roman 
     purchased_date=03/03/2024 purchased_price=20.0 stock=17, ISBN=9001101 
     author=Leon Tolstoi title=Ana Karenina book_category=Roman 
     purchased_date=13/03/2024 purchased_price=13.0 stock=35, 
     ISBN=134367yu author=Dritero Agolli title=Shkelqimi dhe Renia e shokut Zylo 
     book_category=Roman purchased_date=16/03/2024 purchased_price=10.0 stock=30, 
     ISBN=18211821 author=Shekspiri title=Hamleti book_category=Drame 
     purchased_date=16/03/2024 purchased_price=10.0 stock=7]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void testBookAddition_And_Monthly_BooksBought_Statistics2() throws
            IOException, DateNotValidException, BookNotFoundException,
            ISBNnotValidException, ClassNotFoundException, ParseException {

        Book book = new Book("134367yu","Dritero Agolli",
                "Shkelqimi dhe Renia e shokut" + " " + "Zylo",
                "Roman","17/03/2024",10,
                20,7);

        manager.addBooks(book);

        String start_date = "8/03/2024";
        String end_date = "16/03/2024";

        // different dates where books are bought
        // values between (upper bound included for end date)
        // same thing if lower bound was included

        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
    [ISBN=9001101 author=Leon Tolstoi title=Ana Karenina book_category=Roman 
    purchased_date=13/03/2024 purchased_price=13.0 stock=35, ISBN=134367yu 
    author=Dritero Agolli title=Shkelqimi dhe Renia e shokut Zylo 
    book_category=Roman purchased_date=16/03/2024 purchased_price=10.0 stock=37, 
    ISBN=18211821 author=Shekspiri title=Hamleti book_category=Drame 
    purchased_date=16/03/2024 purchased_price=10.0 stock=7]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void testBookAddition_And_Monthly_BooksBought_Statistics_invalid_dates() throws
            IOException, DateNotValidException, BookNotFoundException,
            ISBNnotValidException, ClassNotFoundException {

        Book book = new Book("134367yu","Dritero Agolli",
                "Shkelqimi dhe Renia e shokut" + " " + "Zylo",
                "Roman","16/03/2024",10,
                20,7);

        manager.addBooks(book);

        String start_date = "20/02/2024";
        String end_date = "22/02/2024";
        // end date smaller than min date stored in file
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Monthly_Statistics_of_BooksBought(start_date,end_date));
        assertEquals("No books were bought from" + " " +
                start_date + " " + "to" + " " + end_date,exception.getMessage());
    }

    @Test
    public void testBookAddition_And_Daily_BooksBought_Statistics() throws
            IOException, DateNotValidException, BookNotFoundException,
            ISBNnotValidException, ClassNotFoundException, ParseException {

        Book book = new Book("134367yu","Dritero Agolli",
                "Shkelqimi dhe Renia e shokut" + " " + "Zylo",
                "Roman","1/01/2025",10,
                20,1);

        manager.addBooks(book);

        String date = "16/03/2024"; // last date in file(upper bound)

        ArrayList<Book> actual_value = this.manager.
                Daily_Statistics_ofBooksBought(date);
        String expected = """
               [ISBN=134367yu author=Dritero Agolli title=Shkelqimi dhe Renia e shokut Zylo
               book_category=Roman
               purchased_date=16/03/2024 purchased_price=10.0 stock=45,
               ISBN=18211821 author=Shekspiri title=Hamleti book_category=Drame 
               purchased_date=16/03/2024 purchased_price=10.0 stock=7]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_for_incomes() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException,
            BookNotFoundException {

        librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                        "Zylo",1);

        String start_date = "2/03/2024";
        String end_date = "17/04/2024"; // min and max dates stored
        // in file included

        double expected_income = 470.0;

        assertEquals(expected_income, manager.certain_period_incomes(start_date,
                end_date),0.1);
    }

    @Test
    public void test_with_non_existing_dates() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException,
            BookNotFoundException {

        librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                        "Zylo",7);
        String start_date = "18/04/2024";
        String end_date = "1/05/2024"; // outside bounds dates

        double expected_income = 0.0;

        assertEquals(expected_income, manager.certain_period_incomes(start_date,
                end_date),0.1);
    }

    @Test
    public void test_for_Existing_date() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException,
            BookNotFoundException {

        librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                        "Zylo",7);

        String date = "17/04/2024";
        double expected_incomes = 380.0;

        assertEquals(expected_incomes, manager.daily_incomes(date), 0.1);
    }

    @Test
    public void test_for_incomes_without_filters() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException,
            BookNotFoundException {

        /*librarian.Bill("17/04/2024",
                "Shkelqimi dhe Renia e shokut" + " " +
                        "Zylo",14);*/

        double expected_incomes = 1030.0;

        assertEquals(expected_incomes, manager.incomes_Without_filters(), 0.1);
    }

    @Test
    public void test_for_profits() throws DateNotValidException,
            ParseException, IOException, ClassNotFoundException,
            BillNotFoundException {

        // lower bound
        String start_date = "2/03/2024";
        String end_date = "2/03/2024";

        double profit = manager.profit(start_date,end_date);
        assertEquals(195,profit,0.1);
    }

    @Test
    public void test_for_profits2() throws DateNotValidException,
            ParseException, IOException, ClassNotFoundException,
            BillNotFoundException {

        // lower bound and upper bound
        String start_date = "2/03/2024";
        String end_date = "17/04/2024";

        double profit = manager.profit(start_date,end_date);
        assertEquals(-125,profit,0.1);
    }

}
