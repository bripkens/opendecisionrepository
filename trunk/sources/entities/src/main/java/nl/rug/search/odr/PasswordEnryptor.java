
package nl.rug.search.odr;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class PasswordEnryptor {

    private static final PasswordEncryptor encryptor = new BasicPasswordEncryptor();

    public static String encryptPassword(String password) {
        return encryptor.encryptPassword(password);
    }

    public static boolean checkPassword(String plainPassword, String encryptedPassword) {
        return encryptor.checkPassword(plainPassword, encryptedPassword);
    }
}
