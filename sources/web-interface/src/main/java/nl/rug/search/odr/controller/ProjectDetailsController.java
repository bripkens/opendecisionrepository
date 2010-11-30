package nl.rug.search.odr.controller;

import com.sun.faces.util.MessageFactory;
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
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.SelectItemComparator;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.StateLocal;

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
    private StateLocal sl;
    @EJB
    private DecisionLocal dl;
    @EJB
    private DecisionTemplateLocal dtl;
    private long id;
    private Project project;
    private String decisionName;
    private String projectId;
    private long iterationToDeleteId;
    private long concernToDeleteId;
    private String iterationToDeleteName;
    private String concernToDeleteName;
    public static final String USED_DECISION_NAME = "nl.rug.search.odr.USED_DECISION_NAME";
    private Decision decisionToDelete;
    private List<State> states;
    private State state;
    private State initialState;
    // <editor-fold defaultstate="collapsed" desc="construction">




    @PostConstruct
    public void postConstruct() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            ErrorUtil.showNotAuthenticatedError();
            return;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

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

        states = sl.getCommonStates();
        initialState = sl.getInitialState();
        state = initialState;
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


    public void decisionAddCanceled(ActionEvent e) {
        decisionName = null;
        state = initialState;
    }




    private boolean memberIsInProject() {
        return getProjectMember() != null;
    }




    public void addDecision() {
        if (!isValid()) {
            ErrorUtil.showUknownError();
            return;
        }

        Decision d = new Decision();
        d.setName(decisionName);
        d.setTemplate(dtl.getSmallestTemplate());
        Version initialVersion = new Version();
        Date currentdate = new Date();
        initialVersion.setDecidedWhen(currentdate);
        initialVersion.setDocumentedWhen(currentdate);
        initialVersion.setState(state);

        Collection<ProjectMember> initiators = new ArrayList<ProjectMember>(1);
        initiators.add(getProjectMember());
        initialVersion.setInitiators(initiators);

        d.addVersion(initialVersion);
        project.addDecision(d);
        pl.merge(project);

        decisionName = null;

        // reloading the project to get the new id
        project = pl.getById(project.getId());

        state = initialState;

        JsfUtil.addJavascriptCall("odr.hideDecisionAddForm();");
    }




    public void showDeleteIterationConfirmation(ActionEvent e) {
        Iteration it = (Iteration) e.getComponent().getAttributes().get("iteration");

        iterationToDeleteId = it.getId();
        iterationToDeleteName = it.getName();

        JsfUtil.addJavascriptCall("odr.showIterationDeleteForm();");
    }




    public long getConcernToDeleteId() {
        return concernToDeleteId;
    }




    public void setConcernToDeleteId(long concernToDeleteId) {
        this.concernToDeleteId = concernToDeleteId;
    }




    public String getConcernToDeleteName() {
        return concernToDeleteName;
    }




    public void setConcernToDeleteName(String concernToDeleteName) {
        this.concernToDeleteName = concernToDeleteName;
    }





    public void showDeleteConcernConfirmation(ActionEvent e) {
        Concern co = (Concern) e.getComponent().getAttributes().get("concern");

        concernToDeleteId = co.getId();
        concernToDeleteName = co.getName();

        JsfUtil.addJavascriptCall("odr.showConcernDeleteForm();");
    }




    public void deleteConcern() {
        for (Concern co : project.getConcerns()) {
            if (co.getId().equals(concernToDeleteId)) {
                project.removeConcern(co);
                break;
            }
        }

        pl.merge(project);

        JsfUtil.addJavascriptCall("odr.popup.hide();");
    }




    public void deleteIteration() {
        for (Iteration it : project.getIterations()) {
            if (it.getId().equals(iterationToDeleteId)) {
                project.removeIteration(it);
                break;
            }
        }

        pl.merge(project);

        JsfUtil.addJavascriptCall("odr.popup.hide();");
    }




    public void deleteDecision(Decision d) {
        decisionToDelete = d;

        JsfUtil.addJavascriptCall("odr.showDecisionDeleteForm();");
    }




    public void deleteDecisionConfirmed(Decision d) {
        dl.delete(d);

        JsfUtil.addJavascriptCall("odr.popup.hide();");
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="validator">

    public void checkDecisionName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String newName = value.toString().trim();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        for (Decision d : project.getDecisions()) {
            if (d.getName().equalsIgnoreCase(newName)) {
                throw new ValidatorException(MessageFactory.getMessage(
                        fc,
                        USED_DECISION_NAME,
                        new Object[]{
                            MessageFactory.getLabel(fc, uic)
                        }));
            }
        }
    }




    public Decision getDecisionToDelete() {
        return decisionToDelete;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter">



    public List<SelectItem> getStates() {
        List<SelectItem> items = new ArrayList<SelectItem>(states.size());

        for (State state : states) {
            items.add(new SelectItem(state.getId(), state.getStatusName()));
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }




    public String getShowIterationLink(Iteration it) {
        return new QueryStringBuilder().setUrl(Filename.ITERATION_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, it.getId()).
                toString();
    }



    public String getRelationshipViewLink() {
        return new QueryStringBuilder().setUrl(Filename.DRAWING).
                append(RequestParameter.ID, project.getId()).
                toString();
    }


    public String getShowConcernLink(Concern co) {
        return new QueryStringBuilder().setUrl(Filename.CONCERN_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_ID, co.getId()).
                toString();
    }




    public String getCreateIterationLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_ITERATION).
                append(RequestParameter.ID, project.getId()).
                toString();
    }




    public String getCreateConcernLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_CONCERNS).
                append(RequestParameter.ID, project.getId()).
                toString();
    }




    public String getEditIterationLink(Iteration it) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_ITERATION).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, it.getId()).
                toString();
    }




    public String getEditConcernLink(Concern co) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_CONCERNS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_ID, co.getId()).
                toString();
    }




    public String getDetailDecisionLink(Decision d) {
        return new QueryStringBuilder().setUrl(Filename.DECISION_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, d.getId()).
                append(RequestParameter.VERSION_ID, d.getCurrentVersion().getId()).
                toString();
    }




    public String getEditDecisionLink(Decision d) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_DECISION).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, d.getId()).
                append(RequestParameter.VERSION_ID, d.getCurrentVersion().getId()).
                toString();
    }




    public String getCreateDecisionLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_DECISION).
                append(RequestParameter.ID, project.getId()).
                appendBolean(RequestParameter.CREATE).
                toString();
    }




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
        return RequestParameter.PROJECT_PATH_SHORT.substring(1).
                concat(project.getName()).
                concat("/").
                concat(RequestParameter.UPDATE);
    }




    public String getDeleteLink() {
        return RequestParameter.PROJECT_PATH_SHORT.substring(1).
                concat(project.getName()).
                concat("/").
                concat(RequestParameter.DELETE);
    }




    public Collection<Iteration> getIterations() {
        if (project == null) {
            return null;
        }

        Collection<Iteration> unmodifiableCollection = project.getIterations();

        if (unmodifiableCollection.isEmpty()) {
            return Collections.emptyList();
        }

        List<Iteration> iterations = new ArrayList(unmodifiableCollection);

        Collections.sort(iterations, Collections.reverseOrder(new Iteration.EndDateComparator()));

        return iterations;
    }




    public Collection<Concern> getConcerns() {
        if (project == null) {
            return null;
        }

        Collection<Concern> unmodifiableCollection = project.getConcerns();

        if (unmodifiableCollection.isEmpty()) {
            return Collections.emptyList();
        }
        List<Concern> concerns = new ArrayList(unmodifiableCollection);


        Collections.sort(concerns, new Concern.GroupDateComparator());


        Long groupId = null;
        List<Concern> onlyNewest = new ArrayList<Concern>();

        for (Concern con : concerns) {
            if (groupId == null || !groupId.equals(con.getGroup())) {
                onlyNewest.add(con);
                groupId = con.getGroup();
            }
        }

        Collections.reverse(onlyNewest);

        return onlyNewest;
    }




    public String getDecisionName() {
        return decisionName;
    }




    public void setDecisionName(String decisionName) {
        this.decisionName = decisionName;
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

        List<Decision> resultDecisions = new ArrayList<Decision>(decisions.size());

        for (Decision decision : decisions) {
            if (!decision.isRemoved()) {
                resultDecisions.add(decision);
            }
        }

        Collections.sort(resultDecisions, Collections.reverseOrder(new Decision.DocumentedWhenComparator()));

        return resultDecisions;
    }




    public String getState() {
        return state.getId().toString();
    }




    public void setState(String stateString) {
        long stateId = Long.parseLong(stateString);

        for (State state : states) {
            if (state.getId().equals(stateId)) {
                this.state = state;
                return;
            }
        }
    }
    // </editor-fold>
}
