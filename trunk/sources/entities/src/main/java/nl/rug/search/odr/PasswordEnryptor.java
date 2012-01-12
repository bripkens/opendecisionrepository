package nl.rug.search.odr;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class PasswordEnryptor {

    private PasswordEnryptor() {
    }
    
    private static final PasswordEncryptor ENCRYPTOR = new BasicPasswordEncryptor();

    public static String encryptPassword(String password) {
        return ENCRYPTOR.encryptPassword(password);
    }

    public static boolean checkPassword(String plainPassword, String encryptedPassword) {
        return ENCRYPTOR.checkPassword(plainPassword, encryptedPassword);
    }
}
