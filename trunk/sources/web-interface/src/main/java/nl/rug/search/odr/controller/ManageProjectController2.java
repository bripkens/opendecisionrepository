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
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.user.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@SessionScoped
public class ManageProjectController2 extends AbstractManageController {

    // <editor-fold defaultstate="collapsed" desc="attributes">
    @EJB
    private ProjectLocal pl;
    @EJB
    private UserLocal ul;
    @EJB
    private StakeholderRoleLocal srl;
    private String name;
    private String description;
    private String memberInput;
    private Collection<ProjectMember> projectMembers;
    private ProjectMember currentUser;
    private Project sourceProject;
    public static final String USEDPROJECTNAME_ID =
            "nl.rug.search.odr.validator.ProjectNameValidator.DUPLICATEPROJECTNAME";

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="requests">

    @Override
    protected void handleCreateRequest() {
        sourceProject = new Project();
        currentUser = new ProjectMember();
        currentUser.setPerson(ul.getById(AuthenticationUtil.getUserId()));
        currentUser.setRole(srl.getSomePublicRole());
    }

    @Override
    protected boolean handleUpdateRequest(long id) {
        sourceProject = pl.getById(id);

        if (sourceProject == null) {
            return false;
        }

        name = sourceProject.getName();
        description = sourceProject.getDescription();

        long userId = AuthenticationUtil.getUserId();

        for (ProjectMember pm : sourceProject.getMembers()) {
            if (pm.getPerson().getId() == userId) {
                currentUser = pm;
            } else {
                projectMembers.add(pm);
            }
        }

        return true;
    }

    @Override
    protected boolean handleDeleteRequest(long id) {
        name = "delete request";
        return true;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="execution">
    public void addMember(ActionEvent e) {

        if (isMember(memberInput)) {
            // TODO: notify user that the person is already a member?
            return;
        } else if (!ul.isUsed(memberInput)) {
            // TODO: notify user that this person is not registered?
            return;
        }

        ProjectMember p = new ProjectMember();

        StakeholderRole role = srl.getSomePublicRole();
        p.setRole(role);

        Person person = ul.getByEmail(memberInput);
        memberInput = null;
        p.setPerson(person);

        projectMembers.add(p);
    }

    public void removeMember(ActionEvent e) {
        ProjectMember pm = (ProjectMember) e.getComponent().getAttributes().get("member");

        projectMembers.remove(pm);
    }

    @Override
    protected boolean handleCreateExecution() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected boolean handleUpdateExecution() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected boolean handleConfirmedDeleteExecution(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="resets">
    @Override
    protected void reset() {
        name = description = memberInput = null;
        projectMembers = new ArrayList<ProjectMember>();
    }

    @Override
    protected void resetRequestDependent() {
        currentUser = null;
        sourceProject = null;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="validators">
    public void checkProjectName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        String newName = value.toString();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        if (sourceProject != null && sourceProject.getName() != null) {
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

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter and setter">
    private boolean isMember(String value) {
        if (value.equalsIgnoreCase(currentUser.getPerson().getEmail())) {
            return true;
        }

        for (ProjectMember member : projectMembers) {
            if (value.equalsIgnoreCase(member.getPerson().getEmail())) {
                return true;
            }
        }

        return false;
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

    @Override
    protected boolean isPreviousEntitySet() {
        return sourceProject != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemberInput() {
        return memberInput;
    }

    public void setMemberInput(String memberInput) {
        this.memberInput = memberInput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ProjectMember> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Collection<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public ProjectMember getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ProjectMember currentUser) {
        this.currentUser = currentUser;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="messages">
    @Override
    protected String getSuccessMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['project.manage.success']}", String.class);
    }

    @Override
    protected String getFailMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['project.manage.fail']}", String.class);
    }
    // </editor-fold>
}
