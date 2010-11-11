
package nl.rug.search.odr;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class PasswordEnryptor {

    private static final PasswordEncryptor encryptor = new StrongPasswordEncryptor();

    public static String encryptPassword(String password) {
        return encryptor.encryptPassword(password);
    }

    public static boolean checkPassword(String plainPassword, String encryptedPassword) {
        return encryptor.checkPassword(plainPassword, encryptedPassword);
    }
}
