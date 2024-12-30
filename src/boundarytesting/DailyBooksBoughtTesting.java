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

public class DailyBooksBoughtTesting {
    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_dailyStatistics_with_lower_bound() throws DateNotValidException, ParseException, IOException,BookNotFoundException,
            ClassNotFoundException {

        String date = "23/02/2024"; // first date in file(lower bound)

        ArrayList<Book> actual_value = this.manager.
                Daily_Statistics_ofBooksBought(date);
        String expected = """
                [ISBN=111aaa   author=Albert Kamy
                title=I huaji   book_category=Roman
                purchased_date=23/02/2024   purchased_price=10.0   stock=16]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_dailyStatistics_with_upper_bound() throws DateNotValidException, IOException, ParseException
            ,BookNotFoundException, ClassNotFoundException {

        String date = "13/03/2024"; // last date a book is bought,
        // upper bound

        ArrayList<Book> actual_value = this.manager.
                Daily_Statistics_ofBooksBought(date);
        String expected = """
                [ISBN=9001101   author=Leon Tolstoi   title=Ana Karenina
                book_category=Roman   purchased_date=13/03/2024
                purchased_price=13.0   stock=35]""";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_dailyStatistics_with_between_value() throws DateNotValidException, IOException,
            ParseException,BookNotFoundException, ClassNotFoundException {

        String date = "3/03/2024"; // in between value where books are
        // bought

        ArrayList<Book> actual_value = this.manager.
                Daily_Statistics_ofBooksBought(date);
        String expected = """
            [ISBN=144ty58   author=Fjodor Dostojevksi
            title=Krim dhe ndeshkim   book_category=Roman
            purchased_date=3/03/2024   purchased_price=10.0   stock=34,
            ISBN=70000000   author=Miguel De Servantes   title=Don Kishoti 
              book_category=Roman  \s
            purchased_date=3/03/2024   purchased_price=20.0   stock=23]""";

        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.toString().trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_dailyStatistics_with_outside() {

        String date = "22/02/2024"; // outside bound, no book is bought

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Daily_Statistics_ofBooksBought(date));
        assertEquals("No books were bought during" + " " + date,
                exception.getMessage());
    }

    @Test
    public void test_dailyStatistics_with_outside_bound() {
        String date = "24/02/2024"; // in between value where books
        // are not bought

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.Daily_Statistics_ofBooksBought(date));
        assertEquals("No books were bought during" + " " + date,
                exception.getMessage());    }
}
