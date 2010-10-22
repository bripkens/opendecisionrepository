package nl.rug.search.odr.controller.decision;

import java.util.Enumeration;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.controller.AbstractController;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;

/**
 * 
 * Controller should be view / conversation scoped. Unfortunately I only got it working using
 * session scope. See the following web sites for some additional information
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
    // </editor-fold>

    private Project project;

    // <editor-fold defaultstate="collapsed" desc="construction">
    /**
     * Since this class is session scoped the postConstruct method will be called by isValid
     */
    public void postConstruct() {
        initSteps();

        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance().getExternalContext().getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, pl);

        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            project = result.getProject();
        } else if (project == null) {
            System.out.println("Invalid");
            result.executeErrorAction();
        }
    }




    private void initSteps() {
        essentialsStep = new EssentialsStep(this);
        templateRelatedStep = new TemplateRelatedStep(this);
        relationshipsStep = new RelationshipsStep(this);
        statesStep = new StatesStep(this);
        confirmationStep = new ConfirmationStep(this);

        if (STEP_ORDER[0] == EssentialsStep.class) {
            currentStep = essentialsStep;
        } else if (STEP_ORDER[0] == TemplateRelatedStep.class) {
            currentStep = templateRelatedStep;
        } else if (STEP_ORDER[0] == RelationshipsStep.class) {
            currentStep = relationshipsStep;
        } else if (STEP_ORDER[0] == StatesStep.class) {
            currentStep = statesStep;
        } else if (STEP_ORDER[0] == ConfirmationStep.class) {
            currentStep = confirmationStep;
        }
    }




    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="step navigation">
    public void nextStep() {
        int nextStepId = getStepId(currentStep) + 1;

        setStep(nextStepId);
    }




    public void previousStep() {
        int previousStepId = getStepId(currentStep) - 1;

        setStep(previousStepId);
    }




    private void setStep(int id) {
        currentStep.blur();

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

        currentStep.focus();

        JsfUtil.refreshPage();
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="submit/reset">
    @Override
    protected void reset() {
    }




    @Override
    protected boolean execute() {
        return false;
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="getter that return calculated values">
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
        postConstruct();
        return project != null;
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
