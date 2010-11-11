package nl.rug.search.odr;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EmailValidatorTest {

    @Test
    public void testValidEmail() {
        assertTrue(EmailValidator.isValidEmailAddress("foo@foo.de"));
    }

    @Test
    public void testInvalidEmail1() {
        assertFalse(EmailValidator.isValidEmailAddress("dassa"));
    }

    @Test
    public void testInvalidEmail2() {
        assertFalse(EmailValidator.isValidEmailAddress("dassa@dasdsa"));
    }

    @Test
    public void testInvalidEmail3() {
        assertFalse(EmailValidator.isValidEmailAddress("dassadasdsa.de"));
    }
}
