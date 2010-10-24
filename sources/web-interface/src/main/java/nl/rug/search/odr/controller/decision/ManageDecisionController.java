package nl.rug.search.odr.controller.decision;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.ActionResult;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.controller.AbstractController;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;

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
        RelationshipsStep.class,
        StatesStep.class,
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="pojo attributes">
    private Project project;

    private Decision decision;
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
            project = result.getProject();
        } else if (project == null) {
            result.executeErrorAction();
        }
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

        if (essentialsStep.getDecisionTemplateAsObject().
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

        JsfUtil.refreshPage();
    }




    @Override
    protected void reset() {
        if (essentialsStep != null) {
            essentialsStep.dispose();
        }

        if (templateRelatedStep != null) {
            templateRelatedStep.dispose();
        }

        if (relationshipsStep != null) {
            relationshipsStep.dispose();
        }

        if (statesStep != null) {
            statesStep.dispose();
        }

        if (confirmationStep != null) {
            confirmationStep.dispose();
        }

        setStep(0);
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
        return false;
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="getter which return calculated values">
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
        return project != null;
    }




    public boolean isUpdateRequest() {
        return decision != null;
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
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="getter">
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
