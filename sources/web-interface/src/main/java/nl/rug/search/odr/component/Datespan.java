package nl.rug.search.odr.component;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@FacesComponent(value = "nl.rug.search.odr.component.Datespan")
public class Datespan extends UIInput implements NamingContainer {

    public static final String START_AFTER_END = "Date: start date after end date";




    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }




    @Override
    protected Object getConvertedValue(FacesContext context, Object newSubmittedValue) throws ConverterException {
        UIInput startComponent = (UIInput) findComponent("startDate");
        UIInput endComponent = (UIInput) findComponent("endDate");

        Date startDate = (Date) startComponent.getValue();
        Date endDate = (Date) endComponent.getValue();

        if (startDate.after(endDate)) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JsfUtil.evaluateExpressionGet("#{page['datetimePicker.wrongOrder']}", String.class), JsfUtil.evaluateExpressionGet("#{page['datetimePicker.wrongOrder']}", String.class)));
        }

        return new Object();
    }




    @Override
    protected void validateValue(FacesContext context, Object newValue) {
        if (newValue == null) {
            FacesContext.getCurrentInstance().validationFailed();
        }

        super.validateValue(context, newValue);
    }




    @Override
    public Object getSubmittedValue() {
        return this;
    }
}
