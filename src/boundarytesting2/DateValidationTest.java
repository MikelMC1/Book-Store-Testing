package boundarytesting2;

import exceptions.DateNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import useraccess.Librarian;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class DateValidationTest {
    private Librarian librarian;

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException {
        librarian = new Librarian();
    }

    @Test
    public void testValidDate() {
        try {
            librarian.isValid("01/01/2000");
        } catch (DateNotValidException e) {
            fail("Valid date should not throw exception");
        }
    }

    @Test
    public void testInvalidDateFormat() {
        assertThrows(DateNotValidException.class, () -> librarian.isValid("31-12-2024"));
    }

    @Test
    public void testNonexistentDate() {
        assertThrows(DateNotValidException.class, () -> librarian.isValid("29/02/2023"));
    }

    @Test
    public void testValidDateBoundary() {
        try {
            librarian.isValid("31/12/2024");
        } catch (DateNotValidException e) {
            fail("Valid date should not throw exception");
        }
    }
}

