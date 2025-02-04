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

// Bill.txt content
// 2/03/2024, Krim dhe ndeshkim, 2 book/s
// 2/03/2024, Krim dhe ndeshkim, 2 book/s
// 2/03/2024, Krim dhe ndeshkim, 2 book/s
// 2/03/2024, I huaji, 1 book/s
// 8/03/2024, Don Kishoti, 2 book/s
// 8/03/2024, Krim dhe ndeshkim, 1 book/s
// 14/03/2024, I huaji, 3 book/s

public class MonthlyStatisticsTesting {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        manager = new Manager();
    }

    @Test
    public void test_with_equal_dates_within_bounds() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {
        String start_date = "2/03/2024";
        String end_date = "2/03/2024"; // first date in file

        String actual_value = this.manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("""
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, I huaji, 1 book/s""",actual_value);
    }

    @Test
    public void test_with_equal_dates_within_bounds2() throws DateNotValidException, IOException, ParseException, BillNotFoundException {
        String start_date = "14/03/2024";
        String end_date = "14/03/2024"; // last date a book is sold,
        // upper bound

        String actual_value = this.manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("14/03/2024, I huaji, 3 book/s",actual_value);
    }

    @Test
    public void test_with_equal_dates_within_bounds3() throws DateNotValidException, IOException, ParseException, BillNotFoundException {
        String start_date = "8/03/2024";
        String end_date = "8/03/2024"; // in between value where books are
        // sold

        String actual_value = this.manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("8/03/2024, Don Kishoti, 2 book/s\n" +
                "8/03/2024, Krim dhe ndeshkim, 1 book/s",actual_value);
    }

    @Test
    public void test_with_equal_dates_outside_bounds() throws DateNotValidException, IOException, ParseException, BillNotFoundException {
        String start_date = "1/03/2024";
        String end_date = "1/03/2024"; // outside bounds where no book is sold

        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("No books are sold from" + " " + start_date + " " +
                "to" +
                " " + end_date,actual_value);
    }

    @Test
    public void test_with_equal_dates_inside_bounds() throws DateNotValidException, IOException, ParseException, BillNotFoundException {
        String start_date = "7/03/2024";
        String end_date = "7/03/2024"; // inside bounds where no book is sold

        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("No books are sold from" + " " + start_date + " " +
                "to" +
                " " + end_date,actual_value);
    }

    // we can pick two different dates even if they are outside bounds(one or both),
    // it may happen that in one day between them books are sold or it
    // may happen that they are not sold this is relative we for sure know
    // that in case start date is bigger than the max date saved in file or
    // end date is smaller than the min date stored in file no book is sold
    // during that period

    @Test
    public void test_with_dates_within_bounds() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {
        String start_date = "2/03/2024";
        String end_date = "14/03/2024"; // different dates where books are sold
        // lower and upper bound included

        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("""
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, I huaji, 1 book/s
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s
                14/03/2024, I huaji, 3 book/s""", actual_value);

    }

    @Test
    public void test_with_dates_within_bounds2() throws DateNotValidException, IOException, ParseException, BillNotFoundException {
        String start_date = "2/03/2024";
        String end_date = "9/03/2024";
        // different dates where books are sold
        // values between (lower bound included for start date
        // same thing if upper bound was included, for simplicity
        // I am taking only this case

        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("""
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, I huaji, 1 book/s
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s""", actual_value);
    }

    @Test
    public void test_with_dates_within_bounds3() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {
        String start_date = "3/03/2024";
        String end_date = "13/03/2024";
        // date in between where books are sold but no lower bound or upper
        // bound included

        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("""
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s""", actual_value);
    }

    @Test
    public void test_with_dates_outside_bounds() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {
        String start_date = "1/03/2024";
        String end_date = "15/03/2024";
        // dates outside bounds where books are sold but no lower bound
        // or upper bound included

        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("""
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, Krim dhe ndeshkim, 2 book/s
                2/03/2024, I huaji, 1 book/s
                8/03/2024, Don Kishoti, 2 book/s
                8/03/2024, Krim dhe ndeshkim, 1 book/s
                14/03/2024, I huaji, 3 book/s""", actual_value);
    }

    @Test
    public void test_with_dates_outside_bounds2() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {
        String start_date = "17/03/2024";
        String end_date = "18/03/2024";
        // start date larger than max date stored currently in file
        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("No books are sold from" + " " + start_date + " " +
                "to" +
                " " + end_date,actual_value);
    }

    @Test
    public void test_with_dates_outside_bounds3() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {
        String start_date = "23/02/2024";
        String end_date = "1/03/2024";
        // end date smaller than min date stored in file
        String actual_value = manager.getMonthly_Statistics_of_BooksSold(start_date,
                end_date);
        assertEquals("No books are sold from" + " " + start_date + " " +
                "to" +
                " " + end_date,actual_value);
    }

}
