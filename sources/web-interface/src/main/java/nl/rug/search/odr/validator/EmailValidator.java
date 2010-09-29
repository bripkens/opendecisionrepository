package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.EjbUtil;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.user.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EmailValidator implements Validator {

    private UserLocal ul;
    
    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String USEDEMAIL_ID =
            "nl.rug.search.odr.validator.EmailValidator.DUPLICATEEMAIL";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String WRONG_EMAIL_FORMAT_ID =
            "nl.rug.search.odr.validator.EmailValidator.WRONGEMAILFORMAT";

    public EmailValidator() {
        try {
            ul = EjbUtil.lookUp(Helper.JNDI_NAME, UserLocal.class);
        } catch (RuntimeException ex) {
            throw new FacesException(ex);
        }
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String email = value.toString();



        if (!StringValidator.isValid(email, false)) {
            return;
        }

        if (!nl.rug.search.odr.EmailValidator.isValidEmailAddress(email)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    WRONG_EMAIL_FORMAT_ID,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
        }

        if (ul.isUsedForFullRegistration(email)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    USEDEMAIL_ID,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
        }
    }

    @EJB(name = Helper.JNDI_NAME, beanInterface = UserLocal.class)
    private class Helper {

        public static final String JNDI_NAME = "nl.rug.search.odr.validator.EmailValidator.Helper";
    }
}
