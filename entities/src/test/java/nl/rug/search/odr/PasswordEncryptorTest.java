package nl.rug.search.odr;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class PasswordEncryptorTest {

    @Test
    public void testEncryptPassword() {
        String password = "ultra-secret-password-which-not-even-the-cia-could-have-thinked-of";

        assertNotSame(password, PasswordEnryptor.encryptPassword(password));
    }

    @Test
    public void testEncryptAndCheckpassword() {
        String password = "woooooooooow-soooo-secret";

        String encryptedPassword = PasswordEnryptor.encryptPassword(password);

        assertTrue(PasswordEnryptor.checkPassword(password, encryptedPassword));
    }
}
