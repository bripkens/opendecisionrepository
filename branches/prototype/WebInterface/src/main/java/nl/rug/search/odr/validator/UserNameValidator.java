package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.odr.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UserNameValidator implements Validator {

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

            ul = (UserLocal) ic.lookup("java:comp/env/" + UserNameValidatorHelper.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }


    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        if (value == null) {
            return;
        }

        String name = value.toString().trim();

        if (name.isEmpty() || !ul.isRegistered(name)) {
            return;
        }

        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                USEDUSERNAME_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }
}
