
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StringValidator {

    public static boolean isValid(String value) {
        return isValid(value, true);
    }

    public static boolean isValid(String value, boolean throwException) {
        if (value == null) {
            if (throwException) {
                throw new NullPointerException("String validation failed.");
            }

            return false;
        } else if (value.trim().isEmpty()) {
            if (throwException) {
                throw new RuntimeException("String validation failed.");
            }

            return false;
        }

        return true;
    }

}
