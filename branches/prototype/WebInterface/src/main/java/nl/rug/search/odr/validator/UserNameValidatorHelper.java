
package nl.rug.search.odr.validator;

import javax.ejb.EJB;
import nl.rug.search.odr.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@EJB(name=UserNameValidatorHelper.JNDI_NAME, beanInterface=UserLocal.class)
public class UserNameValidatorHelper {

    public static final String JNDI_NAME = "nl.rug.search.odr.validator.UserNameValidatorHelper";

}
