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
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestParameter;
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
    private Project sourceProject;
    private Collection<ProjectMember> projectMembers;
    private Collection<Person> proposedPersons;

    private String previousName;

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
        name = description = autoCompleteInputValue = previousName = null;
        proposedPersons = new ArrayList<Person>();
        projectMembers = new ArrayList<ProjectMember>();
    }

    @Override
    protected boolean execute() {
        sourceProject.setName(name);
        sourceProject.setDescription(description);

        for(ProjectMember member : sourceProject.getMembers()) {
            sourceProject.removeMember(member);
        }

        for (ProjectMember member : projectMembers) {
            sourceProject.addMember(member);
        }
        sourceProject.addMember(currentUser);

        pl.createProject(sourceProject);

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

        for (ProjectMember member : projectMembers) {
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

    public String getPreviousName() {
        return previousName;
    }

    public boolean isValidRequest() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            return false;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String param = request.getParameter(RequestParameter.PROJECT_ID);

        if (param == null) {
            if (sourceProject == null || sourceProject.getId() != null) {
                reset();
                sourceProject = new Project();
                currentUser = new ProjectMember();
                currentUser.setPerson(ul.getById(AuthenticationUtil.getUserId()));
                currentUser.setRole(srl.getSomePublicRole());
                return true;
            }

            return true;
        }



        long projectid;
        try {
            projectid = Long.parseLong(param);
        } catch (NumberFormatException ex) {
            return false; // id can't be parsed
        }

        if (sourceProject != null && sourceProject.getId() == (Long) projectid) {
            return true;
        }

        reset();

        Project p = pl.getById(projectid);

        if (p == null) {
            return false; // id does not exist
        }

        sourceProject = p;
        long userId = AuthenticationUtil.getUserId();

        name = p.getName();
        previousName = name;
        description = p.getDescription();

        for (ProjectMember pm : p.getMembers()) {
            if (pm.getPerson().getId() == userId) {
                currentUser = pm;
            } else {
                projectMembers.add(pm);
            }
        }

        if (currentUser == null) {
            return false; // not part of the group
        }

        return true;
    }
}
