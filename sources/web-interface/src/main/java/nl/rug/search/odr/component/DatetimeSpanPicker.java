package nl.rug.search.odr.component;

import com.sun.faces.util.MessageFactory;
import java.util.Date;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@FacesComponent(value = "nl.rug.search.odr.component.DatetimeSpanPicker")
public class DatetimeSpanPicker extends UIInput implements NamingContainer {

    public static final String START_AFTER_END = "nl.rug.search.odr.DatetimeSpanPicker";




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

        if (startDate != null && endDate != null && startDate.after(endDate)) {
            throw new ConverterException(MessageFactory.getMessage(
                    context,
                    START_AFTER_END));

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
