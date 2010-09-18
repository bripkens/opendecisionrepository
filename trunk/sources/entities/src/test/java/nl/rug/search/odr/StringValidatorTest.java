package nl.rug.search.odr;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StringValidatorTest {

    @Test
    public void testValidString() {
        assertTrue(StringValidator.isValid("dasda"));
    }

    @Test
    public void testValidStringFalse() {
        assertTrue(StringValidator.isValid("dasda", false));
    }

    @Test
    public void testValidStringTrue() {
        assertTrue(StringValidator.isValid("dasda", true));
    }

    @Test(expected=NullPointerException.class)
    public void testInValidNull() {
        StringValidator.isValid(null);
    }

    @Test(expected=NullPointerException.class)
    public void testInValidNullTrue() {
        StringValidator.isValid(null, true);
    }

    public void testInValidNullFalse() {
        assertFalse(StringValidator.isValid(null, false));
    }

    @Test(expected=RuntimeException.class)
    public void testInValidEmpty() {
        StringValidator.isValid("      ");
    }

    @Test(expected=RuntimeException.class)
    public void testInValidEmptyTrue() {
        StringValidator.isValid("     ", true);
    }

    public void testInValidEmptyFalse() {
        assertFalse(StringValidator.isValid("    ", false));
    }
}
