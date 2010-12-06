package nl.rug.search.odr.controller.decision;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Action;
import nl.rug.search.odr.ActionResult;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.controller.AbstractController;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.decision.VersionLocal;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.util.AuthenticationUtil;

/**
 * 
 * Controller should be view / conversation scoped. Unfortunately I only got it working using
 * session scope. See the following web sites for some additional information regarding
 * the use of view scope and dynamic ui:include
 *
 * http://forums.java.net/jive/thread.jspa?threadID=74533
 * http://www.icefaces.org/JForum/posts/list/17579.page
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@SessionScoped
@ManagedBean
public class ManageDecisionController extends AbstractController {

    // <editor-fold defaultstate="collapsed" desc="wizard settings">
    public static final Class<?>[] STEP_ORDER = new Class<?>[]{
        EssentialsStep.class,
        TemplateRelatedStep.class,
        StatesStep.class,
        RelationshipsStep.class,
        ConfirmationStep.class
    };
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="step variables">
    private WizardStep currentStep;

    private EssentialsStep essentialsStep;

    private TemplateRelatedStep templateRelatedStep;

    private RelationshipsStep relationshipsStep;

    private StatesStep statesStep;

    private ConfirmationStep confirmationStep;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private ProjectLocal pl;

    @EJB
    private DecisionTemplateLocal dtl;

    @EJB
    private StateLocal sl;

    @EJB
    private DecisionLocal dl;

    @EJB
    private VersionLocal vl;

    @EJB
    private RelationshipTypeLocal rtl;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="pojo attributes">
    private static final long serialVersionUID = 1l;

    private ProjectMember member;

    private Project project;

    private Decision decision;

    private String initialDecisionName;

    private Version version;

    private State initialState;
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="construction">
    @PostConstruct
    public void postConstruct() {
        
        initSteps();
    }




    private void initSteps() {
        essentialsStep = new EssentialsStep(this);
        templateRelatedStep = new TemplateRelatedStep(this);
        relationshipsStep = new RelationshipsStep(this);
        statesStep = new StatesStep(this);
        confirmationStep = new ConfirmationStep(this);

        setStep(0);
    }




    /**
     * Since this class is session scoped the postConstruct method will be called by isValid
     */
    public void setUp() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, pl);

        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            setUpDecisionSpecific(result);
        } else if (project == null) {
            result.executeErrorAction();
        } else if (!pl.isMember(AuthenticationUtil.getUserId(), project.getId())) {
            project = null;
            decision = null;
            version = null;
        }
    }




    private void setUpDecisionSpecific(RequestAnalyserDto requestAnalyser) {
        project = requestAnalyser.getProject();
        member = requestAnalyser.getMember();

        pl.makeTransient(project);

        String decisionIdParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.DECISION_ID);

        String versionIdParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.VERSION_ID);

        String createParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.CREATE);

        if (createParameter != null) {
            prepareProjosForCreateRequest();
            if (getStepId(currentStep) != 0) {
                setStep(0);
                JsfUtil.addJavascriptCall("odr.refresh();");
            }
            currentStep.focus();
            return;
        } else if (decision != null && decisionIdParameter == null && versionIdParameter == null) {
            return;
        }

        long decisionId = -1;
        long versionId = -1;

        try {
            decisionId = Long.parseLong(decisionIdParameter);
            versionId = Long.parseLong(versionIdParameter);
        } catch (NumberFormatException ex) {
            ErrorUtil.showInvalidIdError();
            return;
        }

        for (Decision decision : project.getDecisions()) {
            if (decision.getId().equals(decisionId)) {
                dl.makeTransient(decision);
                this.decision = decision;
                dl.makeTransient(this.decision);
                this.initialDecisionName = decision.getName();
                break;
            }
        }

        if (decision == null) {
            ErrorUtil.showIdNotRegisteredError();
            return;
        }

        for (Version version : decision.getVersions()) {
            if (version.getId().equals(versionId)) {
                vl.makeTransient(version);
                this.version = version;

                vl.makeTransient(this.version);
                initialState = version.getState();
                if (getStepId(currentStep) != 0) {
                    setStep(0);
                    JsfUtil.addJavascriptCall("odr.refresh();");
                }

                currentStep.focus();
                return;
            }
        }

        ErrorUtil.showIdNotRegisteredError();
    }




    private void prepareProjosForCreateRequest() {
        decision = new Decision();
        version = new Version();
        decision.addVersion(version);
        version.setState(sl.getInitialState());
        Date now = new Date();
        version.setDecidedWhen(now);
        version.setDocumentedWhen(now);
        version.addInitiator(member);
        initialDecisionName = null;
        initialState = version.getState();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="navigation">





    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        NavigationBuilder navi = new NavigationBuilder();
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        navi.setProject(project);
        navi.setDecision(decision);
        navi.setVersion(version);
        if (isUpdateRequest()) {
            navi.setOption(Action.EDIT);
        } else{
            navi.setOption(Action.CREATE);
        }
        return navi.getNavigationBar();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="step navigation">



    public void nextStep() {
        int nextStepId = getStepId(currentStep) + 1;

        if (shouldStepBeSkipped(nextStepId)) {
            nextStepId++;
        }

        navigateToStep(nextStepId);
    }




    public void previousStep() {
        int previousStepId = getStepId(currentStep) - 1;

        if (shouldStepBeSkipped(previousStepId)) {
            previousStepId--;
        }

        navigateToStep(previousStepId);
    }




    private boolean shouldStepBeSkipped(int nextStepId) {
        if (STEP_ORDER[nextStepId] != TemplateRelatedStep.class) {
            return false;
        }

        if (essentialsStep.getDecisionTemplateAsObject() == null || essentialsStep.getDecisionTemplateAsObject().
                getComponents().
                isEmpty()) {
            return true;
        }

        return false;
    }




    private void navigateToStep(int id) {
        currentStep.blur();

        setStep(id);

        currentStep.focus();

        JsfUtil.refreshPage();
    }




    private void setStep(int id) {
        if (STEP_ORDER[id] == EssentialsStep.class) {
            currentStep = essentialsStep;
        } else if (STEP_ORDER[id] == TemplateRelatedStep.class) {
            currentStep = templateRelatedStep;
        } else if (STEP_ORDER[id] == RelationshipsStep.class) {
            currentStep = relationshipsStep;
        } else if (STEP_ORDER[id] == StatesStep.class) {
            currentStep = statesStep;
        } else if (STEP_ORDER[id] == ConfirmationStep.class) {
            currentStep = confirmationStep;
        }
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="submit/reset">
    @Override
    public void resetForm(ActionEvent e) {
        super.resetForm(e);

        if (isUpdateRequest()) {
            decision = dl.getById(decision.getId());
            initialDecisionName = decision.getName();
            version = vl.getById(version.getId());
            initialState = version.getState();
        } else {
            prepareProjosForCreateRequest();
        }

        setStep(0);

        currentStep.focus();

        JsfUtil.refreshPage();
    }




    @Override
    protected void reset() {
        // empty, this reset method is not appropriate for the wizard
    }




    /**
     * When the user is not on the confirmation page, then a press
     * on the submit button should bring him there
     * @return
     */
    @Override
    public ActionResult submitForm() {
        if (currentStep.getClass() != STEP_ORDER[STEP_ORDER.length - 1]) {
            navigateToStep(STEP_ORDER.length - 1);

            return null;
        } else {
            return super.submitForm();
        }
    }




    @Override
    protected boolean execute() {
        if (!isUpdateRequest()) {
            project.addDecision(decision);
        } else if (!initialState.equals(version.getState())) {
            createNewVersion();
        } else {
            version.setDocumentedWhen(new Date());
            removeOldRelationships();
        }

        pl.merge(project);

        project = pl.getById(project.getId());


        for (Decision d : project.getDecisions()) {
            if (d.getName().equals(decision.getName())) {
                decision = d;
                break;
            }
        }

        for (Version v : decision.getVersions()) {
            if (v.getDocumentedWhen().equals(version.getDocumentedWhen())) {
                version = v;
                break;
            }
        }

        String url = new QueryStringBuilder().setUrl(Filename.DECISION_DETAILS_WITH_LEADING_SLASH).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, decision.getId()).
                append(RequestParameter.VERSION_ID, version.getId()).
                toString();

        JsfUtil.redirect(url);

        project = null;
        decision = null;
        initialDecisionName = null;
        version = null;
        initialState = null;

        setStep(0);

        return true;
    }




    private void createNewVersion() {
        decision.removeVersion(version);

        Version newVersion = new Version();
        newVersion.setDocumentedWhen(new Date());
        newVersion.setDecidedWhen(version.getDecidedWhen());
        newVersion.setInitiators(version.getInitiators());
        newVersion.setConcerns(version.getConcerns());
        newVersion.setState(version.getState());
        newVersion.setRemoved(version.isRemoved());

        for (Relationship eachRelationship : version.getOutgoingRelationships()) {
            Relationship newRelationship = new Relationship();
            newRelationship.setTarget(eachRelationship.getTarget());
            newRelationship.setType(eachRelationship.getType());
            newVersion.addOutgoingRelationship(newRelationship);
        }



        Version previousVersion = vl.getById(version.getId());
        version = newVersion;

        if (newVersion.getDecidedWhen().equals(previousVersion.getDecidedWhen())) {
            newVersion.setDecidedWhen(new Date(newVersion.getDecidedWhen().getTime() + 1));
        }

        vl.persist(version);

        decision.addVersion(newVersion);
        decision.addVersion(previousVersion);
    }




    private void removeOldRelationships() {
        Version previousVersion = vl.getById(version.getId());

        for (Relationship oldRelationship : previousVersion.getOutgoingRelationships()) {
            if (!version.getOutgoingRelationships().contains(oldRelationship)) {
                Version oldTarget = oldRelationship.getTarget();
                oldRelationship.setTarget(null);
                vl.merge(oldTarget);
            }
        }
    }



    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter which return calculated values">
    public boolean isUpdateRequest() {
        return initialDecisionName != null;
    }




    public boolean isLastStep() {
        return currentStep.getClass() == STEP_ORDER[STEP_ORDER.length - 1];
    }




    public boolean isFirstStep() {
        return currentStep.getClass() == STEP_ORDER[0];
    }




    private int getStepId(WizardStep step) {
        for (int i = 0; i < STEP_ORDER.length; i++) {
            if (STEP_ORDER[i] == step.getClass()) {
                return i;
            }

        }

        return -1;
    }




    public boolean isValid() {
        setUp();
        return decision != null && version != null;
    }




    public String getHeadline() {
        if (isUpdateRequest()) {
            return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.update']}", String.class);
        } else {
            return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.create']}", String.class);
        }
    }



    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter which return EJBs">
    DecisionTemplateLocal getDecisionTemplateLocal() {
        return dtl;
    }




    ProjectLocal getProjectLocal() {
        return pl;
    }




    RelationshipTypeLocal getRelationshipTypeLocal() {
        return rtl;
    }




    DecisionLocal getDecisionLocal() {
        return dl;
    }




    StateLocal getStateLocal() {
        return sl;
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="getter">
    @Override
    public boolean showMessage() {
        return false;
    }




    @Override
    protected String getSuccessMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.success.message']}", String.class);
    }




    @Override
    protected String getFailMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.fail.message']}", String.class);
    }




    public ConfirmationStep getConfirmationStep() {
        return confirmationStep;
    }




    public WizardStep getCurrentStep() {
        return currentStep;
    }




    public EssentialsStep getEssentialsStep() {
        return essentialsStep;
    }




    public RelationshipsStep getRelationshipsStep() {
        return relationshipsStep;
    }




    public StatesStep getStatesStep() {
        return statesStep;
    }




    public TemplateRelatedStep getTemplateRelatedStep() {
        return templateRelatedStep;
    }




    public Project getProject() {
        return project;
    }




    public Decision getDecision() {
        return decision;
    }




    public String getInitialDecisionName() {
        return initialDecisionName;
    }




    public State getInitialState() {
        return initialState;
    }




    public Version getVersion() {
        return version;
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="setter">
    public void setConfirmationStep(ConfirmationStep confirmationStep) {
        this.confirmationStep = confirmationStep;
    }




    public void setCurrentStep(WizardStep currentStep) {
        this.currentStep = currentStep;
    }




    public void setEssentialsStep(EssentialsStep essentialsStep) {
        this.essentialsStep = essentialsStep;
    }




    public void setRelationshipsStep(RelationshipsStep relationshipsStep) {
        this.relationshipsStep = relationshipsStep;
    }




    public void setStatesStep(StatesStep statesStep) {
        this.statesStep = statesStep;
    }




    public void setTemplateRelatedStep(TemplateRelatedStep templateRelatedStep) {
        this.templateRelatedStep = templateRelatedStep;
    }
    // </editor-fold>
}



