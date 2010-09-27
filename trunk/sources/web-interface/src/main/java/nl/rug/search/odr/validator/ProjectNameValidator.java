package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import java.io.Serializable;
import java.util.Collections;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.EjbUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectNameValidator implements Validator, Serializable {

    private ProjectLocal pl;
    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String USEDPROJECTNAME_ID =
            "nl.rug.search.odr.validator.ProjectNameValidator.DUPLICATEPROJECTNAME";

    private String previousName;

    public ProjectNameValidator() {
        try {
            pl = EjbUtil.lookUp(Helper.JNDI_NAME, ProjectLocal.class);
        } catch (RuntimeException ex) {
            throw new FacesException(ex);
        }
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        String name = value.toString();

        if (!StringValidator.isValid(name, false)) {
            return;
        }

        System.out.println(previousName);

        if (previousName != null && !previousName.isEmpty()) {
            if (name.equalsIgnoreCase(previousName)) {
                return;
            }
        }


        if (!pl.isUsed(name)) {
            return;
        }



        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                USEDPROJECTNAME_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }

    public String getPreviousName() {
        return previousName;
    }

    public void setPreviousName(String previousName) {
        this.previousName = previousName;
    }

    

    @EJB(name = Helper.JNDI_NAME, beanInterface = ProjectLocal.class)
    private class Helper {

        public static final String JNDI_NAME = "nl.rug.search.odr.validator.ProjectNameValidator.Helper";
    }
}