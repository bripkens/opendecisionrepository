package nl.rug.search.odr.controller.decision;

import nl.rug.search.odr.WizardStep;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EssentialsStep implements WizardStep {

    private final ManageDecisionController wizard;




    public EssentialsStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/essentials.xhtml";
    }




    @Override
    public void focus() {
    }




    @Override
    public void blur() {
    }




    @Override
    public void dispose() {
    }
}
