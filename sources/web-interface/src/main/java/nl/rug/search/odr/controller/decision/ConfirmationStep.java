package nl.rug.search.odr.controller.decision;

import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ConfirmationStep implements WizardStep {

    private final ManageDecisionController wizard;




    public ConfirmationStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/confirmation.xhtml";
    }




    @Override
    public void focus() {
        
    }




    @Override
    public void blur() {
    }




    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.confirmation']}", String.class);
    }




}
