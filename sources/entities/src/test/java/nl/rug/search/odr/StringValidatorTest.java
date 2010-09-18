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
    public void testInValidString() {
        StringValidator.isValid(null);
    }

    @Test(expected=NullPointerException.class)
    public void testInValidStringTrue() {
        StringValidator.isValid(null, true);
    }
}
