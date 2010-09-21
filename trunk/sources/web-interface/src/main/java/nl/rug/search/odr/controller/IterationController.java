package nl.rug.search.odr.controller;


import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.IterationLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.Iteration;


/**
 *
 * @author Stefan
 */
@RequestScoped
@ManagedBean
public class IterationController extends AbstractController {

    @EJB
    private IterationLocal iterationLocal;
    private String name, description;
    private Date startDate, endDate = new Date();

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

        iterationLocal.addIteration(i);
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

    
}
