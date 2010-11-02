package nl.rug.search.odr.controller.decision;

import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StatesStep implements WizardStep {

    private final ManageDecisionController wizard;




    public StatesStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/states.xhtml";
    }




    @Override
    public void focus() {
    }




    @Override
    public void blur() {
    }


    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.state']}", String.class);
    }
}
