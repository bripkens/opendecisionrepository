
package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class ManageProjectController extends AbstractController {

    @EJB
    private ProjectLocal pl;

    private String name;
    private String description;

    private Collection<Person> proposedPersons;

    public ManageProjectController() {
        proposedPersons = new ArrayList<Person>();
    }

    @Override
    protected String getSuccessMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['project.manage.success']}", String.class);
    }

    @Override
    protected String getFailMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['project.manage.fail']}", String.class);
    }

    @Override
    protected void reset() {
        name = description = null;
    }

    @Override
    protected boolean execute() {
        Project p = new Project();
        p.setName(name);
        p.setDescription(description);

        pl.createProject(p);

        reset();

        return true;
    }

    public void autoCompleteInputValueChanged(ValueChangeEvent e) {
        if (e.getNewValue().equals(e.getOldValue())) {
            return;
        }
        
        if (e.getNewValue().toString().trim().isEmpty()) {
            proposedPersons.clear();
            return;
        }

        proposePersons(e.getNewValue().toString());
    }

    private void proposePersons(String input) {
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
