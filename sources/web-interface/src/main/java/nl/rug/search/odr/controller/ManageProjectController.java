package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.project.StakeholderRoleLocal;
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

    @EJB
    private StakeholderRoleLocal srl;
    
    private String name;
    private String description;
    private String autoCompleteInputValue;
    private ProjectMember currentUser;
    private Collection<ProjectMember> projectMembers;
    private Collection<Person> proposedPersons;

    @PostConstruct
    public void initForm() {
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        

        currentUser = new ProjectMember();
        currentUser.setPerson(ul.getById(AuthenticationUtil.getUserId()));
        currentUser.setRole(srl.getSomePublicRole());
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
        name = description = autoCompleteInputValue = null;
        proposedPersons = new ArrayList<Person>();
        projectMembers = new ArrayList<ProjectMember>();
        currentUser = null;
    }

    @Override
    protected boolean execute() {
        Project p = new Project();
        p.setName(name);
        p.setDescription(description);

        for(ProjectMember member : projectMembers) {
            p.addMember(member);
        }
        p.addMember(currentUser);

        for(ProjectMember member : p.getMembers()) {
            System.out.println("Project: " + p.getName() + "; Member: " + member.getPerson().getName() + "; Role: " + member.getRole().getName());
        }

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
        proposedPersons = ul.getProposedPersons(input);
    }

    public Collection<SelectItem> getProposedPersons() {
        Collection<SelectItem> items = new ArrayList<SelectItem>();

        for (Person p : proposedPersons) {
            if (!isMember(p.getName())) {
                items.add(new SelectItem(p, p.getName()));
            }
        }

        return items;
    }

    public int getProposedPersonsListLength() {
        return 10;
    }

    private boolean isMember(String value) {
        if (value.equalsIgnoreCase(currentUser.getPerson().getName())) {
            return true;
        }

        for(ProjectMember member : projectMembers) {
            if (value.equalsIgnoreCase(member.getPerson().getName())) {
                return true;
            }
        }

        return false;
    }

    

    public void addMember(ActionEvent e) {

        if (isMember(autoCompleteInputValue)) {
            // TODO: notify user that the person is already a member?
            return;
        } else if (!ul.isRegistered(autoCompleteInputValue)) {
            // TODO: notify user that this person is not registered?
            return;
        }

        ProjectMember p = new ProjectMember();

        StakeholderRole role = srl.getSomePublicRole();
        p.setRole(role);

        Person person = ul.getByName(autoCompleteInputValue);
        autoCompleteInputValue = null;
        p.setPerson(person);

        projectMembers.add(p);
    }

    public void removeMember(ActionEvent e) {
        ProjectMember pm = (ProjectMember) e.getComponent().getAttributes().get("member");

        projectMembers.remove(pm);
    }

    public Collection<SelectItem> getRoles() {
        Collection<SelectItem> roleItems = new ArrayList<SelectItem>();
        Collection<StakeholderRole> roles = srl.getPublicRoles();

        for (StakeholderRole role : roles) {
            SelectItem item = new SelectItem(role, role.getName());
            roleItems.add(item);
        }

        return roleItems;
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

    public ProjectMember getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ProjectMember currentUser) {
        this.currentUser = currentUser;
    }

    
}
