package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.sun.faces.util.MessageFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.ErrorUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben
 */
@ViewScoped
@ManagedBean
public class ProjectDetailsController {

    @EJB
    private ProjectLocal pl;
    @EJB
    private IterationLocal il;
    private long id;
    private Project project;
    private String iterationName;
    private String iterationDescription;
    private String projectId;
    private long iterationToDeleteId;
    private String iterationToDeleteName;

    public static final String USED_ITERATION_NAME
            = "nl.rug.search.odr.controller.ProjectDetailsController.USED_ITERATION_NAME";

    // <editor-fold defaultstate="collapsed" desc="construction">
    @PostConstruct
    public void postConstruct() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            ErrorUtil.showNotAuthenticatedError();
            return;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if (request.getParameter(RequestParameter.ID) != null) {
            projectId = request.getParameter(RequestParameter.ID);
        }

        try {
            id = Long.parseLong(projectId);
        } catch (NumberFormatException e) {
            ErrorUtil.showInvalidIdError();
            return;
        }

        getProject();

        if (project == null) {
            ErrorUtil.showIdNotRegisteredError();
            return;
        } else if (project != null && !memberIsInProject()) {
            ErrorUtil.showNoMemberError();
            return;
        }
    }

    public boolean isValid() {
        return project != null;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="actionlistener">
    public void iterationAddCanceled(ActionEvent e) {
        iterationName = iterationDescription = null;

        JsfUtil.clearMessages();
    }

    private boolean memberIsInProject() {
        return getProjectMember() != null;
    }

    public void rowMemberSelectionListener(RowSelectorEvent event) {
        // TODO: empty until now, just to get the css tag right
    }

    public void rowIterationSelectionListener(RowSelectorEvent event) {
        // TODO: empty until now, just to get the css tag right
    }

    public void rowDecisionSelectionListener(RowSelectorEvent event) {
        // TODO: empty until now, just to get the css tag right
    }

    public void addIteration() {
        if (!isValid()) {
            try {
                JsfUtil.redirect("/error.html");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        Iteration i = new Iteration();
        i.setName(iterationName);
        i.setDescription(iterationDescription);
        i.setStartDate(new Date());                             // TODO change!
        i.setEndDate(new Date(new Date().getTime() + 3));       // TODO change!
        i.setDocumentedWhen(new Date());
        i.setProjectMember(getProjectMember());

        il.addIteration(project, i);

        iterationName = iterationDescription = null;

        // reloading the project to get the new id
        project = pl.getById(project.getId());

        JsfUtil.clearMessages();

        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "hideIterationAddForm();");
    }

    public void showDeleteIterationConfirmation(ActionEvent e) {
        Iteration it = (Iteration) e.getComponent().getAttributes().get("iteration");

        iterationToDeleteId = it.getId();
        iterationToDeleteName = it.getName();

        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "showIterationDeleteForm();");
    }

    public void deleteIteration() {
        for (Iteration it : project.getIterations()) {
            if (it.getId().equals(iterationToDeleteId)) {
                project.removeIteration(it);
                break;
            }
        }

        pl.updateProject(project);

        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "hideModalPopup();");
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="validator">
    public void checkIterationName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String newName = value.toString().trim();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        for (Iteration it : project.getIterations()) {
            if (it.getName().equalsIgnoreCase(newName)) {
                throw new ValidatorException(MessageFactory.getMessage(
                        fc,
                        USED_ITERATION_NAME,
                        new Object[]{
                            MessageFactory.getLabel(fc, uic)
                        }));
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter">
    public ProjectMember getProjectMember() {
        long userId = AuthenticationUtil.getUserId();
        for (ProjectMember pm : project.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                return pm;
            }
        }
        return null;
    }

    public String getDescription() {
        return project.getDescription();
    }

    public Project getProject() {
        project = pl.getById(id);
        return project;
    }

    public Collection<ProjectMember> getProjectMembers() {
        Collection<ProjectMember> copy = new ArrayList<ProjectMember>();

        for (ProjectMember pm : project.getMembers()) {
            if (!pm.isRemoved()) {
                copy.add(pm);
            }
        }
        return copy;
    }

    public String getProjectName() {
        return project.getName();
    }

    public String getUpdateLink() {
        return "project/".concat(project.getName()).concat("/update");
    }

    public String getDeleteLink() {
        return "project/".concat(project.getName()).concat("/delete");
    }

    public Collection<Iteration> getIterations() {
        if (project == null) {
            return null;
        }

        Collection<Iteration> unmodifiableCollection = project.getIterations();

        if (unmodifiableCollection.isEmpty()) {
            return Collections.emptyList();
        }

        List<Iteration> iterations = new ArrayList(unmodifiableCollection.size());

        for (Iteration it : unmodifiableCollection) {
            iterations.add(it);
        }

        Collections.sort(iterations, Collections.reverseOrder(new Iteration.EndDateComparator()));

        return iterations;
    }

    public String getIterationDescription() {
        return iterationDescription;
    }

    public void setIterationDescription(String iterationDescription) {
        this.iterationDescription = iterationDescription;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public long getIterationToDeleteId() {
        return iterationToDeleteId;
    }

    public String getIterationToDeleteName() {
        return iterationToDeleteName;
    }

    public void setIterationToDeleteName(String iterationToDeleteName) {
        this.iterationToDeleteName = iterationToDeleteName;
    }

    public Collection<Decision> getDecisions() {
        Collection<Decision> decisions = project.getDecisions();

        if (decisions.isEmpty()) {
            return Collections.emptyList();
        }

        return decisions;
    }
    // </editor-fold>
}
