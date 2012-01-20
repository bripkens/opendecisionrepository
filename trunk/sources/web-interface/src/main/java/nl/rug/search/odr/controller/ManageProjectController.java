package nl.rug.search.odr.controller;

import com.sun.faces.util.MessageFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.EmailValidator;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.SelectItemComparator;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.user.UserLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ViewScoped
@ManagedBean
public class ManageProjectController {

    public static final String USEDPROJECTNAME_ID =
            "nl.rug.search.odr.controller.ManageProjectController.DUPLICATEPROJECTNAME";

    public static final String INVALIDNAME =
            "nl.rug.search.odr.controller.ManageProjectController.INVALIDPROJECTNAMES";

    public static final String ALREADY_MEMBER =
            "nl.rug.search.odr.controller.ManageProjectController.ALREADYMEMBER";

    @EJB
    private UserLocal userLocal;

    @EJB
    private ProjectLocal projectLocal;

    @EJB
    private StakeholderRoleLocal stakeholderRoleLocal;

    private ProjectMember user;

    private String originalProjectName;
    
    private Project project;

    private List<StakeholderRole> allRoles;
    
    private List<MemberInput> members;
    
    private String addMemberField;

    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        RequestAnalyserDto analysisResult = new RequestAnalyser(request,
                projectLocal).analyse();

        if (analysisResult.isValid()) {
            user = analysisResult.getMember();
            project = analysisResult.getProject();
            originalProjectName = project.getName();
        } else if (AuthenticationUtil.isAuthtenticated()) {
            user = new ProjectMember();
            user.setPerson(userLocal.getById(AuthenticationUtil.getUserId()));
            user.setRole(stakeholderRoleLocal.getSomePublicRole());
            project = new Project();
            project.addMember(user);
        } else {
            analysisResult.executeErrorAction();
            return;
        }

        allRoles = stakeholderRoleLocal.getPublicRoles();
        
        members = new ArrayList<MemberInput>(project.getMembers().size());
        for(ProjectMember pm : project.getMembers()) {
            if (!pm.getPerson().equals(user.getPerson()) && !pm.isRemoved()) {
                members.add(new MemberInput(pm));
            }
        }
    }

    public boolean isValidRequest() {
        return user != null && project != null;
    }

    public List<SelectItem> getAvailableRoles() {
        List<SelectItem> items = new ArrayList<SelectItem>(allRoles.size());

        for (StakeholderRole role : allRoles) {
            SelectItem item;
            item = new SelectItem(role.getId(), role.getName());
            items.add(item);
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }

    public void setUserRole(String roleIdString) {
        long roleId = Long.parseLong(roleIdString);

        for (StakeholderRole role : allRoles) {
            if (role.getId().equals(roleId)) {
                user.setRole(role);
                return;
            }
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectMember getUser() {
        return user;
    }

    public void setUser(ProjectMember user) {
        this.user = user;
    }

    public String getAddMemberField() {
        return addMemberField;
    }

    public void setAddMemberField(String addMemberField) {
        this.addMemberField = addMemberField;
    }

    public List<MemberInput> getMembers() {
        return members;
    }

    public void setMembers(List<MemberInput> members) {
        this.members = members;
    }
    
    public void checkProjectName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        String newName = value.toString().trim();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        if (originalProjectName != null
                && newName.equalsIgnoreCase(originalProjectName)) {
            return;

        }

        if (!isProjectNameValid(newName)) {

            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    INVALIDNAME,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));
        }

        if (!projectLocal.isUsed(newName)) {
            return;
        }

        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                USEDPROJECTNAME_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }

    private boolean isProjectNameValid(String newProjectName) {
        String allNames = JsfUtil.evaluateExpressionGet("#{error['invalid.project.names']}", String.class);
        String[] invalidNames = allNames.split(",");

        for (String word : invalidNames) {
            if (newProjectName.equals(word.trim())) {
                return false;
            }
        }
        return true;
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
        }
    }
    
    private boolean isMember(String value) {
        if (value.equalsIgnoreCase(user.getPerson().getEmail())) {
            return true;
        }

        for (MemberInput member : members) {
            if (value.equalsIgnoreCase(member.getMember().getPerson().getEmail()) &&
                    !member.getMember().isRemoved()) {
                return true;
            }
        }

        return false;
    }
    
    public void addMember() {
        ProjectMember newMember = new ProjectMember();

        if (!EmailValidator.isValidEmailAddress(addMemberField)) {
            return;
        }

        if (isMember(addMemberField)) {
            return;
        } else if (!userLocal.isUsedOverall(addMemberField)) {

            try {
                Person person = userLocal.preRegister(addMemberField);
                newMember.setPerson(person);
            } catch (BusinessException ex) {
            }
        } else {
            for (ProjectMember member : project.getMembers()) {
                if (member.isRemoved() &&
                        member.getPerson().getEmail().equalsIgnoreCase(addMemberField)) {
                    member.setRemoved(false);
                    members.add(new MemberInput(member));
                    addMemberField = null;
                    return;
                }
            }

            Person person = userLocal.getByEmail(addMemberField);
            newMember.setPerson(person);
        }

        StakeholderRole role = stakeholderRoleLocal.getSomePublicRole();
        newMember.setRole(role);

        addMemberField = null;

        project.addMember(newMember);
        members.add(new MemberInput(newMember));
    }
    
    public void removeMember(String email) {
        
        Iterator<MemberInput> it = members.iterator();
        MemberInput member;
        
        while (it.hasNext()) {
            member = it.next();
            
            if (member.getMember().getPerson().getEmail().equalsIgnoreCase(email)) {
                it.remove();
                member.getMember().setRemoved(true);
            }
        }
        
    }
    
    private StakeholderRole getRole(long id) {
        for (StakeholderRole role : allRoles) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        
        throw new IllegalStateException("Wrong id");
    }
    
    public void setUserRoleId(long id) {
        user.setRole(getRole(id));
    }
    
    public long getUserRoleId() {
        return user.getRole().getId();
    }

    public void submit() {
        projectLocal.merge(project);
        JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT.concat(project.getName()));
    }
    
    public void reset() {
        if (project.getId() != null) {
            JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT.concat(project.getName()));
        } else {
            JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT);
        }
    }
    
    public class MemberInput {
        private ProjectMember member;
        private long roleId;

        public MemberInput(ProjectMember member) {
            this.member = member;
            roleId = member.getRole().getId();
        }
        
        public long getRoleId() {
            return roleId;
        }

        public void setRoleId(long roleId) {
            member.setRole(getRole(roleId));
            this.roleId = roleId;
        }

        public ProjectMember getMember() {
            return member;
        }

        public void setMember(ProjectMember member) {
            this.member = member;
        }
    }
}
