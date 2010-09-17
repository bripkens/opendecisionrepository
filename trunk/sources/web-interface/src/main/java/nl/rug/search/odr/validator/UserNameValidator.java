
package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UserNameValidator implements Validator{

    private UserLocal ul;

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String USEDUSERNAME_ID =
            "nl.rug.search.odr.validator.UserNameValidator.DUPLICATEUSERNAME";


    public UserNameValidator() {
        try {
            InitialContext ic = new InitialContext();

            ul = (UserLocal) ic.lookup("java:comp/env/" + Helper.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String name = value.toString();

        if (!StringValidator.isValid(name, false) || !ul.isRegistered(name)) {
            return;
        }

        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                USEDUSERNAME_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }

    @EJB(name = Helper.JNDI_NAME, beanInterface = UserLocal.class)
    private class Helper {

        public static final String JNDI_NAME = "nl.rug.search.odr.validator.UserNameValidator.Helper";
    }
}
