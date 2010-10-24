package nl.rug.search.odr.controller.decision;

import com.sun.faces.util.MessageFactory;
import java.util.Collection;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.DecisionTemplate;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EssentialsStep implements WizardStep {

    // <editor-fold defaultstate="collapsed" desc="constants">
    public static final String USER_DECISION_NAME = "nl.rug.search.odr.USED_DECISION_NAME";

    // <editor-fold defaultstate="collapsed" desc="attributes">
    private final ManageDecisionController wizard;

    private String decisionName;

    private DecisionTemplate decisionTemplate;

    private String oprLink;
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="construction">
    public EssentialsStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="action called by parent component">
    @Override
    public void focus() {
    }




    @Override
    public void blur() {
    }




    @Override
    public void dispose() {
        decisionName = null;
        decisionTemplate = null;
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="validators">
    public void checkDecisionName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String newName = value.toString().
                trim();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        if (wizard.isUpdateRequest() && newName.equalsIgnoreCase(wizard.getDecision().
                getName())) {
            return;
        }

        for (Decision decision : wizard.getProject().
                getDecisions()) {
            if (newName.equalsIgnoreCase(decision.getName())) {
                throw new ValidatorException(MessageFactory.getMessage(
                        fc,
                        USER_DECISION_NAME,
                        new Object[]{
                            MessageFactory.getLabel(fc, uic)
                        }));
            }
        }
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="getter and setter">
    @Override
    public String getFaceletName() {
        return "decisionSteps/essentials.xhtml";
    }




    public String getDecisionName() {
        return decisionName;
    }




    public void setDecisionName(String decisionName) {
        this.decisionName = decisionName;
    }




    public String getDecisionTemplate() {
        if (decisionTemplate != null) {
            return decisionTemplate.getName();
        }

        return null;
    }


    public DecisionTemplate getDecisionTemplateAsObject() {
        return decisionTemplate;
    }



    public void setDecisionTemplate(String decisionTemplate) {
        this.decisionTemplate = wizard.getDecisionTemplateLocal().
                getByName(decisionTemplate);
    }




    public SelectItem[] getDecisionTemplates() {
        DecisionTemplateLocal dtl = wizard.getDecisionTemplateLocal();
        List<DecisionTemplate> allTemplates = dtl.getAll();

        SelectItem[] allTemplatesAsItems = new SelectItem[allTemplates.size()];

        int i = 0;
        for (DecisionTemplate template : allTemplates) {
            SelectItem item = new SelectItem();
            item.setLabel(template.getName());
            item.setValue(template.getName());
            allTemplatesAsItems[i++] = item;
        }

        return allTemplatesAsItems;
    }




    public String getOprLink() {
        return oprLink;
    }




    public void setOprLink(String oprLink) {
        this.oprLink = oprLink;
    }
    // </editor-fold>
}
