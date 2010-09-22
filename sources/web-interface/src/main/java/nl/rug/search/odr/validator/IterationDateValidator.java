package nl.rug.search.odr.validator;

import com.sun.faces.util.MessageFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Stefan
 */
public class IterationDateValidator implements Validator {

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String WRONGDATE_ID = "nl.rug.search.odr.validator.IterationDateValidator.STARTDATEBEFORECURRENTDATE";
    public static final String DATEINPAST_ID = "nl.rug.search.odr.validator.IterationDateValidator.DATEINPAST";

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        //set the now date
        GregorianCalendar gr_now = new GregorianCalendar();
        gr_now.setTime(new Date());

        //set the start date
        Date time = (Date) value;
        GregorianCalendar gr_time = new GregorianCalendar();
        gr_time.setTimeInMillis(time.getTime());

        //startDate auf 12Uhr gesetzt
        gr_time.set(gr_time.get(Calendar.YEAR),
                gr_time.get(Calendar.MONTH),
                gr_time.get(Calendar.DAY_OF_MONTH),
                12, 0);

        //now ist auf aktuellen tag auf 12 uhr gesetzt
        gr_now.set(gr_now.get(Calendar.YEAR),
                gr_now.get(Calendar.MONTH),
                gr_now.get(Calendar.DAY_OF_MONTH),
                0, 1);

//        System.out.println("NOW   " + gr_now.get(Calendar.DAY_OF_YEAR) + "" + gr_now.get(Calendar.YEAR));
//        System.out.println("VALUE   " + gr_time.get(Calendar.DAY_OF_YEAR) + "" + gr_time.get(Calendar.YEAR));

        //Date is in the past
        if (gr_now.after(gr_time)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    DATEINPAST_ID,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
        }

//
//        if (startDateId != null) {
//            UIInput startDateInput = (UIInput) fc.getViewRoot().findComponent(startDateId);
//            Date startDate = null;
//            startDate = (Date) startDateInput.getValue();
//
//            GregorianCalendar cal = new GregorianCalendar();
//            cal.setTime(startDate);
//
//        }

        String startDateId1 = "";
        startDateId1 = (String) uic.getAttributes().get("startDateId");

        String endDateId1 = "";
        endDateId1 = (String) uic.getAttributes().get("endDateId");


        //Startdate before enddate && enddate before startdate

        //enddate calender wird benutzt
        if (startDateId1 != null && value !=null) {

            Date dateValue = (Date) value;

            UIInput startDateInput = (UIInput) fc.getViewRoot().findComponent(startDateId1);
            Date startDate = null;
            startDate = (Date) startDateInput.getValue();


            GregorianCalendar calstart = new GregorianCalendar();
            calstart.setTime(startDate);




            if(startDate.after(dateValue)){
                throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    WRONGDATE_ID,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
            }

        }

    }
}
