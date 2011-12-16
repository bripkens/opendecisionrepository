package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class StringValidator {

    private StringValidator() {
    }

    public static boolean isValid(String value) {
        return isValid(value, true);
    }

    public static boolean isValid(String value, boolean throwException) {
        if (value == null) {
            if (throwException) {
                throw new BusinessException("String validation failed. Value is null.");
            }

            return false;
        } else if (value.trim().isEmpty()) {
            if (throwException) {
                throw new BusinessException("String validation failed. Value is empty.");
            }

            return false;
        }

        return true;
    }
}
