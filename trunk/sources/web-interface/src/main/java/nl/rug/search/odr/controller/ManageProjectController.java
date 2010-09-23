package nl.rug.search.odr.controller;

import com.sun.faces.util.MessageFactory;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.StringValidator;
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
    public static final String USEDPROJECTNAME_ID =
            "nl.rug.search.odr.validator.ProjectNameValidator.DUPLICATEPROJECTNAME";

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
        sourceProject.setName(name);
        sourceProject.setDescription(description);

        for (ProjectMember member : sourceProject.getMembers()) {
            sourceProject.removeMember(member);
        }

        for (ProjectMember member : projectMembers) {
            sourceProject.addMember(member);
        }
        sourceProject.addMember(currentUser);

        pl.createProject(sourceProject);

        resetFields();

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

    public boolean isValidRequest() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            return false;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String paramId = request.getParameter(RequestParameter.PROJECT_ID);
        String paramCreate = request.getParameter(RequestParameter.PROJECT_CREATE);

        if (paramId == null && paramCreate != null) {
            reset();
            sourceProject = new Project();
            currentUser = new ProjectMember();
            currentUser.setPerson(ul.getById(AuthenticationUtil.getUserId()));
            currentUser.setRole(srl.getSomePublicRole());
            return true;
        } else if (paramId == null && paramCreate == null && sourceProject != null && sourceProject.getId() == null) {
            return true;
        }


        if (paramCreate != null) {
            resetFields();
            return false;
        }

        if (paramId == null && sourceProject != null && sourceProject.getId() != null) {
            System.out.println("############################## 3 ###################################");
            initFields(sourceProject);
            return true;
        }


        long projectid;
        try {
            projectid = Long.parseLong(paramId);
        } catch (NumberFormatException ex) {
            System.out.println("############################## 4 ###################################");
            resetFields();
            return false; // id can't be parsed
        }

        if (sourceProject != null && sourceProject.getId() != null && sourceProject.getId().equals(projectid)) {
            System.out.println("############################## 5 ###################################");
            return true;
        }

        reset();

        Project p = pl.getById(projectid);

        if (p == null) {

            System.out.println("############################## 6 ###################################");
            resetFields();
            return false; // id does not exist
        }

        sourceProject = p;
        initFields(p);

        if (currentUser == null) {
            System.out.println("############################## 7 ###################################");
            resetFields();
            return false; // not part of the group
        }

        System.out.println("############################## 8 ###################################");
        return true;
    }

    private void resetFields() {
        name = description = null;
        currentUser = null;
        sourceProject = null;
    }

    private void initFields(Project p) {
        long userId = AuthenticationUtil.getUserId();

        name = p.getName();
        description = p.getDescription();

        for (ProjectMember pm : p.getMembers()) {
            if (pm.getPerson().getId() == userId) {
                currentUser = pm;
            } else {
                projectMembers.add(pm);
            }
        }
    }

    public void checkProjectName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        String newName = value.toString();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        if (sourceProject.getName() != null) {
            if (newName.equalsIgnoreCase(sourceProject.getName())) {
                return;
            }
        }


        if (!pl.isUsed(newName)) {
            return;
        }



        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                USEDPROJECTNAME_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }
}
