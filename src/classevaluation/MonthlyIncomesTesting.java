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

public class MonthlyIncomesTesting {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException,
            IOException, ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_with_existing_dates() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {

        String start_date = "2/03/2024";
        String end_date = "14/03/2024"; // min and max dates stored
        // in file included

        double expected_income = 300.0;

        assertEquals(expected_income, manager.monthly_incomes(start_date,
                end_date),0.1);
    }

    @Test
    public void test_with_existing_dates2() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {

        String start_date = "3/03/2024";
        String end_date = "8/03/2024"; // within bounds dates

        double expected_income = 90.0;

        assertEquals(expected_income, manager.monthly_incomes(start_date,
                end_date),0.1);
    }

    @Test
    public void test_with_non_existing_dates() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {

        String start_date = "20/02/2024";
        String end_date = "1/03/2024"; // outside bounds dates

        double expected_income = 0.0;

        assertEquals(expected_income, manager.monthly_incomes(start_date,
                end_date),0.1);
    }

    @Test
    public void test_for_invalid_start_date() {

        String start_date = "3/03/2024";
        String end_date = "2/03/2024";

        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.monthly_incomes(start_date,end_date));
        assertEquals("Starting date can not be after end date",
                exception.getMessage());
    }

    @Test
    public void test_with_same_start_and_end_date() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {

        String start_date = "8/03/2024";
        String end_date = "8/03/2024";

        double expected_income = 90.0; // 60.0 + 30.0 for this date

        assertEquals(expected_income, manager.monthly_incomes(start_date,
                end_date), 0.1);
    }
    @Test
    public void test_for_invalid_date_format() {
        String start_date = "1/03-2024";
        String end_date = "2/03/2024";
        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.monthly_incomes(start_date,end_date));
        assertEquals("Invalid date format. " +
                "The format required for the date to be entered is"
                + " " + "dd/MM/yyyy", exception.getMessage());
    }

    @Test
    public void test_with_empty_file() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter
                ("BILL.txt"));
        writer.write(""); // Write nothing to file to test
            // for empty file

        String start_date = "2/03/2024";
        String end_date = "14/03/2024";

        BillNotFoundException exception = assertThrows(BillNotFoundException.class,
                () -> manager.monthly_incomes(start_date,end_date));
        assertEquals("No books have been sold till now",
                exception.getMessage());

    }



}
