package boundarytesting2;

import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SalesTotalTest {

    private Librarian librarian;

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException {
        librarian = new Librarian();
    }

    @Test
    public void testEmptySalesFile() {
        assertThrows(BookNotFoundException.class, () -> librarian.getDailyTotal_books("01/01/2024"));
    }

    @Test
    public void testBooksSoldOnBoundaryDate() {
        try {
            assertNotNull(librarian.getDailyTotal_books("8/03/2024"));
        } catch (Exception e) {
            fail("Valid date should return daily book count");
        }
    }

    @Test
    public void testInvalidDateRange() {
        assertThrows(DateNotValidException.class, () -> librarian.getMonthlyTotal_books("01/02/2024", "31/01/2024"));
    }

    @Test
    public void testNoBooksSoldInDateRange() {
        assertThrows(BookNotFoundException.class, () -> librarian.getMonthlyTotal_books("01/01/2023", "01/01/2023"));
    }

    @Test
    public void testTotalBooksSold() {
        try {
            assertNotNull(librarian.getTotal_books());
        } catch (Exception e) {
            fail("Total books sold should return a valid count");
        }
    }
}
