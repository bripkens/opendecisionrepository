package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.EjbUtil;
import nl.rug.search.odr.ProjectLocal;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
public class IterationDateValidator implements Validator {

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String WRONGDATE_ID =
            "nl.rug.search.odr.validator.IterationDateValidator.STARTDATEBEFORECURRENTDATE";


    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        Date time = (Date) value;
        Date now = new Date();

        System.out.println(now);

        if(time.getTime() >= now.getTime()){
            return;
        }

        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                WRONGDATE_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }
}
