
package nl.rug.search.odr.validator;

import javax.ejb.EJB;
import nl.rug.search.odr.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@EJB(name=ProjectNameValidatorHelper.JNDI_NAME, beanInterface=ProjectLocal.class)
public class ProjectNameValidatorHelper {

    public static final String JNDI_NAME = "nl.rug.search.odr.validator.ProjectNameValidatorHelper";
    
}
