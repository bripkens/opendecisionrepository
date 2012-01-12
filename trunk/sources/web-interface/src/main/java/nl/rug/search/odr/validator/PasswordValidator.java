package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Stefan
 */


public class PasswordValidator implements Validator {

    public static final String PASSWORDCONFIRMATIONFAIL = "nl.rug.search.odr.validator.PasswordValidator.PASSWORDCONFIRMATIONFAIL";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
        throws ValidatorException
    {
        // Obtain the client ID of the first password field from f:attribute.
        String passwordId = (String) component.getAttributes().get("passwordId");

        // Find the actual JSF component for the client ID.
        UIInput passwordInput = (UIInput) context.getViewRoot().findComponent(passwordId);

        // Get its value, the entered password of the first field.
        String password = (String) passwordInput.getValue();


        // Cast the value of the entered password of the second field back to String.
        String confirm = (String) value;


        // Compare the first password with the second password.
        if (!password.equals(confirm)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    context,
                    PASSWORDCONFIRMATIONFAIL,
                    new Object[]{
                        MessageFactory.getLabel(context, component)
                    }));
        }

        // You can even validate the minimum password length here and throw accordingly.
        // Or, if you're smart, calculate the password strength and throw accordingly ;)
    }

}