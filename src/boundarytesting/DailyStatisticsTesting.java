package boundarytesting;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Manager;
import java.io.IOException;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.*;

public class DailyStatisticsTesting {
    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_dailyStatistics_with_lower_bound() throws DateNotValidException,
            ParseException, IOException, BillNotFoundException {
        String date = "2/03/2024"; // first date in file(lower bound)

        String actual_value = this.manager.
                getDaily_Statistics_of_BooksSold(date);

        assertEquals("""
            2/03/2024,Krim dhe ndeshkim,2 book/s
            2/03/2024,Krim dhe ndeshkim,2 book/s
            2/03/2024,Krim dhe ndeshkim,2 book/s
            2/03/2024,I huaji,1 book/s""", actual_value);
    }


    @Test
    public void test_dailyStatistics_with_upper_bound() throws DateNotValidException, IOException, ParseException, BillNotFoundException {
        String date = "14/03/2024"; // last date a book is sold, upper bound

        String actual_value = this.manager.
                getDaily_Statistics_of_BooksSold(date);

        assertEquals("14/03/2024,I huaji,3 book/s",actual_value.trim());
    }

    @Test
    public void test_dailyStatistics_with_between_value() throws DateNotValidException, IOException, ParseException, BillNotFoundException {

        String date = "8/03/2024"; // in between value where books are sold

        String actual_value = this.manager.getDaily_Statistics_of_BooksSold(date);
        assertEquals("8/03/2024,Don Kishoti,2 book/s\n" +
                "8/03/2024,Krim dhe ndeshkim,1 book/s",actual_value);
    }

    @Test
    public void test_dailyStatistics_with_outside() throws DateNotValidException, IOException, ParseException, BillNotFoundException {

        String date = "3/03/2024"; // inside bound where no book is sold

        String actual_value = manager.getDaily_Statistics_of_BooksSold(date);
        assertEquals("No books are sold during" + " " + date
                ,actual_value);
    }

    @Test
    public void test_dailyStatistics_with_outside_bound() throws DateNotValidException, IOException, ParseException, BillNotFoundException {

        String date = "1/03/2024"; // outside bound value

        String actual_value = manager.getDaily_Statistics_of_BooksSold(date);
        assertEquals("No books are sold during" + " " + date
                ,actual_value);
    }
}
