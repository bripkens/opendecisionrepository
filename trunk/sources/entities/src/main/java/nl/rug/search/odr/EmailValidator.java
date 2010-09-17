
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EmailValidator {

    public static boolean isValidEmailAddress(String email) {
        return org.apache.commons.validator.EmailValidator.getInstance().isValid(email);
    }
}

