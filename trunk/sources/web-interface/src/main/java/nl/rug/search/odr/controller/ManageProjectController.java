package nl.rug.search.odr.controller;

import com.sun.faces.util.MessageFactory;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.EmailValidator;
import nl.rug.search.odr.Mode;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.util.JsfUtil;
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
@ViewScoped
public class ManageProjectController extends AbstractManageController {

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
            "nl.rug.search.odr.controller.ManageProjectController.DUPLICATEPROJECTNAME";

    public static final String ALREADY_MEMBER =
            "nl.rug.search.odr.controller.ManageProjectController.ALREADYMEMBER";

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
        sourceProject = pl.getById(id);

        if (sourceProject == null) {
            return false;
        }

        name = sourceProject.getName();
        description = sourceProject.getDescription();

        return true;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="execution">


    public void addMember(ActionEvent e) {

        ProjectMember p = new ProjectMember();

        if (!EmailValidator.isValidEmailAddress(memberInput)) {
            return;
        }

        if (isMember(memberInput)) {
            return;
        } else if (!ul.isUsedOverall(memberInput)) {

            try {
                Person person = ul.preRegister(memberInput);
                p.setPerson(person);
            } catch (BusinessException ex) {
            }
        } else {
            for (ProjectMember member : projectMembers) {
                if (member.isRemoved() && member.getPerson().getEmail().equalsIgnoreCase(memberInput)) {
                    member.setRemoved(false);
                    memberInput = null;
                    return;
                }
            }

            Person person = ul.getByEmail(memberInput);
            p.setPerson(person);
        }

        StakeholderRole role = srl.getSomePublicRole();
        p.setRole(role);

        memberInput = null;

        for (ProjectMember member : projectMembers) {
            if (member.getPerson().getId() == p.getId()) {
                member.setRemoved(false);
                return;
            }
        }

        projectMembers.add(p);
    }




    public void removeMember(ActionEvent e) {
        ProjectMember pm = (ProjectMember) e.getComponent().getAttributes().get("member");

        for (ProjectMember member : projectMembers) {
            if (member.getPerson().getId() == pm.getPerson().getId()) {
                member.setRemoved(true);
                return;
            }
        }

        projectMembers.remove(pm);
    }




    public void roleChanged(ValueChangeEvent e) {
        String[] value = e.getNewValue().toString().split(";");

        long userId = Long.parseLong(value[0]);
        long stakeholderRoleId = Long.parseLong(value[1]);

        StakeholderRole newRole = srl.getById(stakeholderRoleId);

        if (currentUser.getPerson().getId().equals(userId)) {
            currentUser.setRole(newRole);
            return;
        }

        for (ProjectMember member : projectMembers) {
            if (member.getPerson().getId().equals(userId)) {
                member.setRole(newRole);
                return;
            }
        }
    }




    @Override
    protected boolean handleCreateExecution() {
        sourceProject.setName(name);
        sourceProject.setDescription(description);

        sourceProject.removeAllMembers();

        for (ProjectMember member : projectMembers) {
            sourceProject.addMember(member);
        }
        sourceProject.addMember(currentUser);

        pl.persist(sourceProject);

        JsfUtil.redirect("/p/".concat(sourceProject.getName()));

        return true;
    }




    @Override
    protected boolean handleUpdateExecution() {
        sourceProject.setName(name);
        sourceProject.setDescription(description);
        sourceProject.removeAllMembers();

        for (ProjectMember member : projectMembers) {
            sourceProject.addMember(member);
        }
        sourceProject.addMember(currentUser);

        pl.merge(sourceProject);

        JsfUtil.redirect("/p/".concat(sourceProject.getName()));

        return true;
    }




    @Override
    protected boolean handleConfirmedDeleteExecution() {
        pl.delete(sourceProject);

        JsfUtil.redirect("/projects.html");

        return true;
    }




    public void delete() {
        handleConfirmedDeleteExecution();
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

        String newName = value.toString().trim();

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




    public void checkEmailAddress(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String email = value.toString();

        if (email == null || email.trim().isEmpty()) {
            return;
        }

        if (!EmailValidator.isValidEmailAddress(email)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    nl.rug.search.odr.validator.EmailValidator.WRONG_EMAIL_FORMAT_ID,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
        }

        if (isMember(email)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    ALREADY_MEMBER,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
            // TODO: notify user that the person is already a member?
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter and setter">


    private boolean isMember(String value) {
        if (value.equalsIgnoreCase(currentUser.getPerson().getEmail())) {
            return true;
        }

        for (ProjectMember member : projectMembers) {
            if (value.equalsIgnoreCase(member.getPerson().getEmail()) && !member.isRemoved()) {
                return true;
            }
        }

        return false;
    }




    public Collection<SelectItem> getRoles(String id) {

        Collection<SelectItem> roleItems = new ArrayList<SelectItem>();
        Collection<StakeholderRole> roles = srl.getPublicRoles();

        for (StakeholderRole role : roles) {
            SelectItem item = new SelectItem(id + ";" + role.getId(), role.getName());
            roleItems.add(item);
        }

        return roleItems;
    }




    public String getRole(String id) {
        long userId = Long.parseLong(id);

        if (currentUser.getPerson().getId().equals(userId)) {
            return currentUser.getPerson().getId() + ";" + currentUser.getRole().getId();
        }

        for (ProjectMember member : projectMembers) {
            if (member.getPerson().getId().equals(userId)) {
                return member.getPerson().getId() + ";" + member.getRole().getId();
            }
        }

        throw new RuntimeException("Can't determine role");
    }




    public String getSelectedRole(String userIdParam) {
        long userId = Long.parseLong(userIdParam);

        if (currentUser.getPerson().getId() == userId) {
            return userId + ";" + currentUser.getRole().getId();
        }

        for (ProjectMember member : projectMembers) {
            if (member.getPerson().getId() == userId) {
                return userId + ";" + member.getRole().getId();
            }
        }

        throw new RuntimeException("Illegal user selected");
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
        JsfUtil.addJavascriptCall("odr.preselect();");

        Collection<ProjectMember> onlyNonRemovedMembers = new ArrayList(projectMembers.size());

        for (ProjectMember pm : projectMembers) {
            if (!pm.isRemoved()) {
                onlyNonRemovedMembers.add(pm);
            }
        }

        return onlyNonRemovedMembers;
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




    @Override
    protected Long getPreviousEntityId() {
        if (sourceProject == null) {
            return null;
        }

        return sourceProject.getId();
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
