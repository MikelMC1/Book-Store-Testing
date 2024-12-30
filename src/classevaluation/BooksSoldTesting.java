package classevaluation;


import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Manager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;


import static org.junit.jupiter.api.Assertions.*;

public class BooksSoldTesting {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        manager = new Manager();
    }

    @Test
    public void test_with_equal_dates() throws DateNotValidException,
            IOException, ParseException,
            BookNotFoundException, BillNotFoundException {

        String ISBN = "144ty58";
        String start_date = "2/03/2024"; // first date in file
        String end_date = "2/03/2024";

        String actual_value = this.manager.
                total_of_BooksSold(ISBN,start_date,
                        end_date);
        String expected = "2/03/2024, 144ty58, Krim dhe ndeshkim, 2 book/s, " +
                "60.0\n" +
                "2/03/2024, 144ty58, Krim dhe ndeshkim, 3 book/s, 90.0";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_different_dates() throws DateNotValidException,
            IOException, ParseException,
            BookNotFoundException, BillNotFoundException {

        String ISBN = "144ty58";
        String start_date = "2/03/2024";
        String end_date = "8/03/2024";

        String actual_value = this.manager.
                total_of_BooksSold(ISBN,start_date,
                        end_date);
        String expected = "2/03/2024, 144ty58, Krim dhe ndeshkim, 2 book/s, " +
                "60.0 2/03/2024, 144ty58, Krim dhe ndeshkim, 3 book/s, 90.0 " +
                "8/03/2024, 144ty58, Krim dhe ndeshkim, 1 book/s, 30.0";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_different_dates2() throws DateNotValidException,
            IOException, ParseException,
            BookNotFoundException, BillNotFoundException {

        String ISBN = "144ty58";
        String start_date = "8/03/2024";
        String end_date = "13/03/2024";

        String actual_value = this.manager.
                total_of_BooksSold(ISBN,start_date,
                        end_date);
        String expected = "8/03/2024, 144ty58, Krim dhe ndeshkim, 1 book/s, 30.0";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_equal_dates2()  {

        String ISBN = "144ty58";
        String start_date = "4/03/2024";
        String end_date = "4/03/2024"; //dates within bounds

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,end_date));
        assertEquals("No books with that ISBN are sold" +
                "till now", exception.getMessage());

    }

    @Test
    public void test_with_equal_dates3()  {

        String ISBN = "144ty58";
        String start_date = "1/03/2024";
        String end_date = "1/03/2024"; //dates outside bounds

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,end_date));
        assertEquals("No books with that ISBN are sold" +
                "till now", exception.getMessage());

    }

    @Test
    public void test_with_different_dates3() throws DateNotValidException,
            IOException, ParseException,
            BookNotFoundException, BillNotFoundException {

        String ISBN = "144ty58";
        String start_date = "1/03/2024";
        String end_date = "15/03/2024"; // both values are taken outside
        // boundaries

        String actual_value = this.manager.
                total_of_BooksSold(ISBN,start_date,
                        end_date);
        String expected = "2/03/2024, 144ty58, Krim dhe ndeshkim, 2 book/s, " +
                "60.0 2/03/2024, 144ty58, Krim dhe ndeshkim, 3 book/s, 90.0 " +
                "8/03/2024, 144ty58, Krim dhe ndeshkim, 1 book/s, 30.0";
        expected = expected.trim().replaceAll("\\s+", " ");
        String actual = actual_value.trim().replaceAll("\\s+", " ");
        assertEquals(expected,actual);
    }

    @Test
    public void test_with_different_dates4() {

        String ISBN = "144ty58";
        String start_date = "3/03/2024";
        String end_date = "7/03/2024"; // values in between

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,end_date));
        assertEquals("No books with that ISBN are sold" +
                "till now", exception.getMessage());
    }

    @Test
    public void test_with_different_dates5()  {

        String ISBN = "144ty58";
        String start_date = "15/03/2024";
        String end_date = "17/03/2024"; // values outside bounds

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,end_date));
        assertEquals("No books with that ISBN are sold" +
                "till now", exception.getMessage());
    }

    // with cases above we have taken cases that not only includes some dates
    // (where books are sold or not) but also an existing and valid ISBN

    @Test
    public void test_for_ivalid_start_date() {

        String ISBN = "144ty58";
        String start_date = "3/03/2024";
        String end_date = "2/03/2024";

        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,
                        end_date));
        assertEquals("Starting date can not be after end date",
                exception.getMessage());
    }

    @Test
    public void test_with_empty_file() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter
                ("BILL.txt"));
        writer.write(""); // Write nothing to file to test
        // for empty file

        String ISBN = "144ty58";
        String start_date = "2/03/2024";
        String end_date = "14/03/2024";

        BillNotFoundException exception = assertThrows(BillNotFoundException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,end_date));
        assertEquals("No books have been sold till now",
                exception.getMessage());

    }

    @Test
    public void test_for_invalid_start_date() {

        String ISBN = "144ty58";
        String start_date = "3/03/2024";
        String end_date = "2/03/2024";

        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.total_of_BooksSold(ISBN,start_date,end_date));
        assertEquals("Starting date can not be after end date",
                exception.getMessage());
    }

}
