package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.user.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@SessionScoped
public class ManageProjectController extends AbstractController {

    @EJB
    private ProjectLocal pl;
    @EJB
    private UserLocal ul;
    private String name;
    private String description;
    private String autoCompleteInputValue;
    private Collection<ProjectMember> projectMembers;
    private Collection<Person> proposedPersons;

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
        name = description = autoCompleteInputValue = null;
        proposedPersons = new ArrayList<Person>();
        projectMembers = new ArrayList<ProjectMember>();
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

        String input = e.getNewValue().toString().trim();

        if (input.isEmpty()) {
            proposedPersons.clear();
            return;
        }

        autoCompleteInputValue = input;

        proposePersons(autoCompleteInputValue);
    }

    private void proposePersons(String input) {
        // TODO
    }

    public Collection<SelectItem> getProposedPersons() {
        Collection<SelectItem> items = new ArrayList<SelectItem>();

        items.add(new SelectItem("1.", "1."));
        items.add(new SelectItem("2.", "2."));
        items.add(new SelectItem("3.", "3."));

        for (Person p : proposedPersons) {
            items.add(new SelectItem(p, p.getName()));
        }

        return items;
    }

    public int getProposedPersonsListLength() {
        return proposedPersons.size() + 3;
    }

    public void addMember(ActionEvent e) {
        ProjectMember p = new ProjectMember();
        Person person = new Person();
        person.setName(autoCompleteInputValue);
        p.setPerson(person);
        projectMembers.add(p);
    }


    public Collection<ProjectMember> getProjectMembers() {
        return projectMembers;
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

    public String getAutoCompleteInputValue() {
        return autoCompleteInputValue;
    }

    public void setAutoCompleteInputValue(String autoCompleteInputValue) {
        this.autoCompleteInputValue = autoCompleteInputValue;
    }

    public void setProjectMembers(Collection<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public void setProposedPersons(Collection<Person> proposedPersons) {
        this.proposedPersons = proposedPersons;
    }
}
