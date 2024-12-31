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

public class DailyIncomesTesting {

    private Manager manager;

    @BeforeEach
    public void setUp() throws BookNotFoundException, IOException,
            ClassNotFoundException {
        this.manager = new Manager();
    }

    @Test
    public void test_for_Existing_date() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {

        String date = "2/03/2024";
        double expected_incomes = 195.0;

        assertEquals(expected_incomes, manager.daily_incomes(date), 0.1);
    }

    @Test
    public void test_for_date_outside_bound() throws DateNotValidException,
            IOException, ParseException, BillNotFoundException {

        String date = "1/03/2024";
        double expected_incomes = 0.0; // this is also for case where file
        // is empty

        assertEquals(expected_incomes,manager.daily_incomes(date),0.1);
    }

    @Test
    public void test_with_empty_file() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter
                ("BILL.txt"));
        writer.write(""); // Write nothing to file to test
        // for empty file

        String date = "2/03/2024";

        BillNotFoundException exception = assertThrows(BillNotFoundException.class,
                () -> manager.daily_incomes(date));
        assertEquals("No books have been sold till now",
                exception.getMessage());

    }

    @Test
    public void test_for_invalid_date_format() {
        String date = "1/03-2024";
        DateNotValidException exception = assertThrows(DateNotValidException.class,
                () -> manager.daily_incomes(date));
        assertEquals("Invalid date format. " +
                "The format required for the date to be entered is"
                + " " + "dd/MM/yyyy", exception.getMessage());
    }


}
