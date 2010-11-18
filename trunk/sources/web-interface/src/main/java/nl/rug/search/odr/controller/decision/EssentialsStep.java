package nl.rug.search.odr.controller.decision;

import com.sun.faces.util.MessageFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.SelectItemComparator;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.DecisionTemplate;
import nl.rug.search.odr.entities.OprLink;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class EssentialsStep implements WizardStep {

    // <editor-fold defaultstate="collapsed" desc="constants">
    public static final String USER_DECISION_NAME = "nl.rug.search.odr.USED_DECISION_NAME";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="attributes">
    private final ManageDecisionController wizard;

    private String decisionName;

    private DecisionTemplate decisionTemplate;

    private String oprLink;

    private Collection<Concern> selectedConcerns;

    private Date decidedWhen;

    private String selectedConcern;
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="construction">

    public EssentialsStep(ManageDecisionController wizard) {
        this.wizard = wizard;
        selectedConcerns = new ArrayList<Concern>();
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="action called by parent component">
    @Override
    public void focus() {
        selectedConcern = null;

        Decision d = wizard.getDecision();

        decisionName = d.getName();
        decisionTemplate = d.getTemplate();

        if (d.getLink() != null) {
            oprLink = d.getLink().getLink();
        } else {
            oprLink = null;
        }

        selectedConcerns = new ArrayList<Concern>(wizard.getVersion().getConcerns());

        decidedWhen = wizard.getVersion().getDecidedWhen();

    }




    @Override
    public void blur() {
        Decision d = wizard.getDecision();

        d.setName(decisionName);
        d.setTemplate(decisionTemplate);

        if (!StringValidator.isValid(oprLink, false)) {
            d.setLink(null);
        } else if (d.getLink() == null) {
            OprLink link = new OprLink();
            link.setLink(oprLink);
            d.setLink(link);
        } else {
            d.getLink().setLink(oprLink);
        }

        Version v = wizard.getVersion();
        v.removeAllConcerns();
        v.setConcerns(selectedConcerns);
        v.setDecidedWhen(decidedWhen);
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="action">
    public void addConcern() {
        for (Concern concern : wizard.getProject().
                getConcerns()) {
            if (concern.getName().
                    equals(selectedConcern) && !selectedConcerns.contains(concern)) {
                selectedConcerns.add(concern);
                selectedConcern = null;
                return;
            }
        }
    }
    // </editor-fold>





    // <editor-fold defaultstate="collapsed" desc="listeners">
    public void removeConcern(long id) {
        for (Concern concern : selectedConcerns) {
            if (concern.getId().
                    equals(id)) {
                selectedConcerns.remove(concern);
                return;
            }
        }
    }
    // </editor-fold>




    // <editor-fold defaultstate="collapsed" desc="validators">
    public void checkDecisionName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String newName = value.toString().
                trim();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        if (wizard.isUpdateRequest() && newName.equalsIgnoreCase(wizard.getInitialDecisionName())) {
            return;
        }

        Collection<Decision> allDecisions = wizard.getProjectLocal().
                getById(wizard.getProject().
                getId()).
                getDecisions();

        for (Decision decision : allDecisions) {
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




    DecisionTemplate getDecisionTemplateAsObject() {
        return decisionTemplate;
    }




    public void setDecisionTemplate(String decisionTemplate) {
        // fix for the datetimepicker which requires in a skip of the validation phase?
        // "Please select" is set as the special "no selection case" which means that this
        // should not be possible
        if (!StringValidator.isValid(decisionTemplate, false)) {
            return;
        } else if (decisionTemplate.equalsIgnoreCase(
                JsfUtil.evaluateExpressionGet("#{form['label.pleaseSelect']}", String.class))){
            return;
        }
        this.decisionTemplate = wizard.getDecisionTemplateLocal().
                getByName(decisionTemplate);
    }




    public SelectItem[] getDecisionTemplates() {
        DecisionTemplateLocal dtl = wizard.getDecisionTemplateLocal();
        List<DecisionTemplate> allTemplates = dtl.getAll();

        Collections.sort(allTemplates, new DecisionTemplate.NameComparator());

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




    public Collection<SelectItem> getAvailableConcerns() {
        Collection<Concern> allConcernsImmutable = wizard.getProject().
                getConcerns();

        List<SelectItem> allConcerns = new ArrayList<SelectItem>(allConcernsImmutable.size());

        for (Concern concern : allConcernsImmutable) {
            if (!selectedConcerns.contains(concern)) {
                SelectItem item = new SelectItem();
                item.setValue(concern.getName());
                item.setLabel(concern.getName());
                allConcerns.add(item);
            }
        }

        Collections.sort(allConcerns, new SelectItemComparator());

        return allConcerns;
    }




    public Collection<Concern> getSelectedConcerns() {

        List<Concern> concerns = (List<Concern>) selectedConcerns;

        Collections.sort(concerns, new Concern.NameComparator());

        return selectedConcerns;
    }




    public Date getDecidedWhen() {
        return decidedWhen;
    }




    public void setDecidedWhen(Date decidedWhen) {
        this.decidedWhen = decidedWhen;
    }




    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.essentials']}", String.class);
    }




    public String getSelectedConcern() {
        return selectedConcern;
    }




    public void setSelectedConcern(String selectedConcern) {
        this.selectedConcern = selectedConcern;
    }
    // </editor-fold>
}
