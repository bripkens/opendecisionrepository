package nl.rug.search.odr;

import java.util.regex.Pattern;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE |
            Pattern.MULTILINE);
    
    public static boolean isValidEmailAddress(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
