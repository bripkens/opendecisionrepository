package nl.rug.search.odr.component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@FacesComponent(value = "nl.rug.search.odr.component.DatetimePicker")
public class DatetimePicker extends UIInput implements NamingContainer {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }




    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        Date date = (Date) getValue();

        if (date == null) {
            date = new Date();
        }

        TimeZone timeZone = TimeZone.getDefault();

        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);
        UIInput hoursComponent = (UIInput) findComponent("hours");
        UIInput minutesComponent = (UIInput) findComponent("minutes");
        UIInput dateComponent = (UIInput) findComponent("date");
        UIOutput timeZoneComponent = (UIOutput) findComponent("timeZone");
        
        hoursComponent.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minutesComponent.setValue(calendar.get(Calendar.MINUTE));
        dateComponent.setValue(date);

        String displayName = timeZone.getDisplayName(timeZone.inDaylightTime(date), TimeZone.SHORT);

        timeZoneComponent.setValue(displayName);

        super.encodeBegin(context);
    }




    @Override
    protected Object getConvertedValue(FacesContext context, Object newSubmittedValue) throws ConverterException {
        UIInput hourComponent = (UIInput) findComponent("hours");
        UIInput minuteComponent = (UIInput) findComponent("minutes");
        UIInput dateComponent = (UIInput) findComponent("date");

        int hour = (Integer) hourComponent.getValue();
        int minute = (Integer) minuteComponent.getValue();
        Date date = (Date) dateComponent.getValue();

        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime();
    }




    @Override
    public Object getSubmittedValue() {
        return this;
    }
}
