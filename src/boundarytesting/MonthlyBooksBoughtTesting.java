package boundarytesting;
import book.Book;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Manager;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MonthlyBooksBoughtTesting {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        manager = new Manager();
    }

    @Test
    public void test_with_equal_dates_within_bounds() throws DateNotValidException, IOException,
            ParseException, BookNotFoundException, ClassNotFoundException
    {
        String start_date = "23/02/2024";
        String end_date = "23/02/2024"; // first date in file

        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
               [ISBN=111aaa author=Albert Kamy title=I huaji book_category=Roman 
               purchased_date=23/02/2024 purchased_price=10.0 stock=4]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected, actual);
    }

    @Test
    public void test_with_equal_dates_within_bounds2() throws
            DateNotValidException, IOException,
            ParseException,BookNotFoundException, ClassNotFoundException {

        String start_date = "13/03/2024";
        String end_date = "13/03/2024"; // last date a book is bought,
        // upper bound
        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
                [ISBN=9001101 author=Leon Tolstoi 
                title=Ana Karenina
                book_category=Roman purchased_date=13/03/2024
                purchased_price=13.0 stock=35]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_equal_dates_within_bounds3() throws DateNotValidException,
            IOException, ParseException, BookNotFoundException, ClassNotFoundException {
        String start_date = "03/03/2024";
        String end_date = "03/03/2024"; // in between value where books
        // are bought

        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
                [ISBN=144ty58 author=Fjodor Dostojevski title=Krim dhe ndeshkim 
                book_category=Roman purchased_date=03/03/2024 purchased_price=10.0 
                stock=13, ISBN=70000000 author=Miguel De Servantes 
                title=Don Kishoti book_category=Roman purchased_date=03/03/2024 
                purchased_price=20.0 stock=17]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_equal_dates_outside_bounds() {

        String start_date = "22/02/2024";
        String end_date = "22/02/2024"; // outside bounds where no book is
        // bought

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Monthly_Statistics_of_BooksBought(start_date,end_date));
        assertEquals("No books were bought from" + " " + start_date + " " +
                        "to" + " " + end_date,
               exception.getMessage() );
    }

    @Test
    public void test_with_equal_dates_within_bounds4() {

        String start_date = "24/02/2024";
        String end_date = "24/02/2024"; // inside bounds where no book is
        // bought

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Monthly_Statistics_of_BooksBought(start_date,end_date));
        assertEquals("No books were bought from" + " " + start_date + " " +
                        "to" + " " + end_date,
                exception.getMessage() );
    }

    // we can pick two different dates even if they are outside bounds(one or both),
    // it may happen that in one day between them books are bought or it
    // may happen that they are not bought this is relative we for sure know
    // that in case start date is bigger than the max date saved in file or
    // end date is smaller than the min date stored in file no book is bought
    // during that period

    @Test
    public void test_with_dates_within_bounds() throws DateNotValidException,
            IOException, ParseException, BookNotFoundException, ClassNotFoundException {
        String start_date = "25/02/2024";
        String end_date = "10/03/2024";

        ArrayList<Book> actual_value = this.manager.Monthly_Statistics_of_BooksBought(start_date, end_date);

        String expected = """
           [ISBN=144ty58 author=Fjodor Dostojevski title=Krim dhe ndeshkim 
           book_category=Roman purchased_date=03/03/2024 
           purchased_price=10.0 stock=13, ISBN=70000000 
           author=Miguel De Servantes title=Don Kishoti 
           book_category=Roman purchased_date=03/03/2024 
           purchased_price=20.0 stock=17]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected, actual);
    }

    @Test
    public void test_with_dates_within_bounds2() throws DateNotValidException,
            IOException, ParseException, BookNotFoundException, ClassNotFoundException {

        String start_date = "23/02/2024";
        String end_date = "03/03/2024";

        // different dates where books are bought
        // values between (lower bound included for start date
        // same thing if upper bound was included, for simplicity
        // I am taking only this case

        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
    [ISBN=111aaa author=Albert Kamy title=I huaji book_category=Roman 
    purchased_date=23/02/2024 purchased_price=10.0 stock=4, 
    ISBN=144ty58 author=Fjodor Dostojevski title=Krim dhe ndeshkim 
    book_category=Roman purchased_date=03/03/2024 purchased_price=10.0 stock=13, 
    ISBN=70000000 author=Miguel De Servantes title=Don Kishoti book_category=Roman 
    purchased_date=03/03/2024 purchased_price=20.0 stock=17]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_dates_within_bounds3() throws DateNotValidException,
            IOException, ParseException, BookNotFoundException, ClassNotFoundException {

        String start_date = "25/02/2024";
        String end_date = "10/03/2024";
        // date in between where books are bought but no lower bound or upper
        // bound included

        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
        [ISBN=144ty58 author=Fjodor Dostojevski title=Krim dhe ndeshkim 
        book_category=Roman purchased_date=03/03/2024 purchased_price=10.0 stock=13, 
        ISBN=70000000 author=Miguel De Servantes title=Don Kishoti book_category=Roman 
        purchased_date=03/03/2024 purchased_price=20.0 stock=17]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected, actual);
    }

    @Test
    public void test_with_dates_outside_bounds() throws DateNotValidException,
            IOException, ParseException,BookNotFoundException, ClassNotFoundException {
        String start_date = "22/02/2024";
        String end_date = "14/03/2024";
        // dates outside bounds where books are bought but no lower bound
        // or upper bound included

        ArrayList<Book> actual_value = this.manager.
                Monthly_Statistics_of_BooksBought(start_date,end_date);
        String expected = """
      [ISBN=111aaa author=Albert Kamy title=I huaji book_category=Roman 
      purchased_date=23/02/2024 purchased_price=10.0 stock=4, ISBN=144ty58 
      author=Fjodor Dostojevski title=Krim dhe ndeshkim book_category=Roman 
      purchased_date=03/03/2024 purchased_price=10.0 stock=13, ISBN=70000000 
      author=Miguel De Servantes title=Don Kishoti book_category=Roman 
      purchased_date=03/03/2024 purchased_price=20.0 stock=17, ISBN=9001101 
      author=Leon Tolstoi title=Ana Karenina book_category=Roman 
      purchased_date=13/03/2024 purchased_price=13.0 stock=35]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_dates_outside_bounds2() {
        String start_date = "18/03/2024";
        String end_date = "19/03/2024";
        // start date larger than max date stored currently in file
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Monthly_Statistics_of_BooksBought(start_date,end_date));
        assertEquals("No books were bought from" + " " +
                start_date + " " + "to" + " " + end_date,exception.getMessage());
    }

    @Test
    public void test_with_dates_outside_bounds3() {
        String start_date = "20/02/2024";
        String end_date = "22/02/2024";
        // end date smaller than min date stored in file
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Monthly_Statistics_of_BooksBought(start_date,end_date));
        assertEquals("No books were bought from" + " " +
                start_date + " " + "to" + " " + end_date,exception.getMessage());
    }





}
