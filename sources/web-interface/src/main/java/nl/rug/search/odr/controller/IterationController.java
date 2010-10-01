package nl.rug.search.odr.controller;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.Iteration;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author Stefan
 */
@SessionScoped
@ManagedBean
public class IterationController extends AbstractController {

    @EJB
    private IterationLocal iterationLocal;

    

    private String name, description;
    private Date startDate, endDate = new Date();
    protected Effect valueChangeEffect2;

    public IterationController() {
        valueChangeEffect2 = new Highlight("#fda505");
        valueChangeEffect2.setFired(true);

        GregorianCalendar startcal = new GregorianCalendar();
        startcal.add(GregorianCalendar.DAY_OF_YEAR, 1);
        startDate = startcal.getTime();

        GregorianCalendar endcal = new GregorianCalendar();
        endcal.add(GregorianCalendar.DAY_OF_YEAR, 14);
        endDate = endcal.getTime();
    }

    @Override
    protected String getSuccessMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['Iteration.success']}", String.class);
    }

    @Override
    protected String getFailMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['Iteration.fail']}", String.class);
    }

    @Override
    protected void reset() {
        name = description = null;
        startDate = endDate = null;
    }

    @Override
    protected boolean execute() {

        Iteration i = new Iteration();
        i.setName(name);
        i.setDescription(description);
        i.setStartDate(startDate);
        i.setEndDate(endDate);

      //  iterationLocal.persistIteration(i);
        return true;
    }

    public String getDescription() {
                return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public IterationLocal getIterationLocal() {
        return iterationLocal;
    }

    public void setIterationLocal(IterationLocal iterationLocal) {
        this.iterationLocal = iterationLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public TimeZone getTimeZone() {
        return java.util.TimeZone.getDefault();
    }
}
