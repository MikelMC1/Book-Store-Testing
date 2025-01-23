package boundarytesting2;

import exceptions.BillNotFoundException;
import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BillCountTest {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException {
        librarian = new Librarian();
    }

    @Test
    public void testEmptyBillFile() {
        assertThrows(BillNotFoundException.class, () -> librarian.nr_Of_Bills_Without_filters());
    }


    @Test
    public void testBillsForSpecificDate() {
        try {
            assertNotNull(librarian.nrOfDailyBills("2/03/2024"));
        } catch (Exception e) {
            fail("Daily bill count should be returned successfully");
        }
    }

    @Test
    public void testInvalidDateRangeForMonthlyBills() {
        assertThrows(DateNotValidException.class, () -> {
            librarian.nrOfMonthlyBills("01/02/2024", "31/01/2024");
        });
    }

    @Test
    public void testNoBillsInDateRange() {
        assertThrows(BillNotFoundException.class, () -> {
            librarian.nrOfMonthlyBills("01/01/2023", "01/01/2023");
        });
    }
}
