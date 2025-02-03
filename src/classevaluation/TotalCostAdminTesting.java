package classevaluation;

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

public class TotalCostAdminTesting {

    private Manager manager;

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException,
            BookNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_with_existing_dates() throws DateNotValidException, IOException,
            ParseException, ClassNotFoundException {

        String start_date = "23/02/2024";
        String end_date = "13/03/2024";

        double expected_total_cost = 1115.0;


        assertEquals(expected_total_cost, manager.getTotalCostAdmin(start_date, end_date), 0.1);
    }

    @Test
    public void test_with_existing_dates_partial_range() throws DateNotValidException, IOException, ParseException, ClassNotFoundException {
        String start_date = "25/02/2024";
        String end_date = "12/03/2024";

        double expected_total_cost = 580.0;

        assertEquals(expected_total_cost, manager.getTotalCostAdmin(start_date, end_date), 0.1);
    }

    @Test
    public void test_with_non_existing_dates() throws DateNotValidException, IOException, ParseException, ClassNotFoundException {
        String start_date = "01/02/2024";
        String end_date = "20/02/2024";

        double expected_total_cost = 0.0; // No books bought in this range

        assertEquals(expected_total_cost, manager.getTotalCostAdmin(start_date,
                end_date), 0.1);
    }

    @Test
    public void test_with_same_start_and_end_date() throws DateNotValidException, IOException, ParseException, ClassNotFoundException {
        String start_date = "23/02/2024";
        String end_date = "23/02/2024";

        double expected_total_cost = 80.0;

        assertEquals(expected_total_cost, manager.getTotalCostAdmin(start_date,
                end_date), 0.1);
    }

    @Test
    public void test_for_invalid_start_date() {
        String start_date = "14/03/2024";
        String end_date = "02/03/2024";

        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.getTotalCostAdmin(start_date, end_date));
        assertEquals("Starting date can not be after end date", exception.getMessage());
    }

    @Test
    public void test_for_invalid_date_format() {
        String start_date = "03-03-2024";
        String end_date = "14/03/2024";

        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.getTotalCostAdmin(start_date, end_date));
        assertEquals("Invalid date format. The format required for the date to be entered is dd/MM/yyyy", exception.getMessage());
    }

    @Test
    public void test_with_empty_books_file() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("books.bin"));
        writer.write(""); // Empty file
        writer.close();

        String start_date = "02/03/2024";
        String end_date = "14/03/2024";

        IOException exception = assertThrows(IOException.class,
                () -> manager.getTotalCostAdmin(start_date, end_date));
        assertEquals("No books found in the file", exception.getMessage());
    }
}
