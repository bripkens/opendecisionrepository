package nl.rug.search.odr.controller.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import nl.rug.search.odr.SelectItemComparator;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipsStep implements WizardStep {

    private final ManageDecisionController wizard;


    private List<Decision> selectedDecisions;


    public RelationshipsStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/relationships.xhtml";
    }




    @Override
    public void focus() {
        selectedDecisions = new ArrayList<Decision>();
    }




    @Override
    public void blur() {
    }

    public void decisionSelectionChangeListener(ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (!StringValidator.isValid(value, false)) {
            return;
        } else if (value.equalsIgnoreCase("Please select")){
            return;
        }

        long id = Long.parseLong(e.getNewValue().toString());

        for(Decision decision : wizard.getProject().getDecisions()) {
            if (decision.getId().equals(id)) {
                selectedDecisions.add(decision);
                return;
            }
        }
    }

    public List<SelectItem> getAvailableDecisions() {
        Collection<Decision> allDecisions = wizard.getProject().getDecisions();

        List<SelectItem> items = new ArrayList<SelectItem>(allDecisions.size());

        for(Decision decision : allDecisions) {
            if (decision.getId().equals(wizard.getDecision().getId())) {
                continue;
            } else if (selectedDecisions.contains(decision)) {
                continue;
            }

            SelectItem item = new SelectItem(decision.getId(), decision.getName());
            items.add(item);
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }

    public List<Decision> getRelationships() {
        Collections.sort(selectedDecisions, new Decision.NameComparator());

        return selectedDecisions;
    }

    public void removeRelationship(long decisionId) {
        for (Decision decision : selectedDecisions) {
            if (decision.getId().equals(decisionId)) {
                selectedDecisions.remove(decision);
                return;
            }
        }
    }

    public List<SelectItem> getRelationshipTypes() {
        List<RelationshipType> types = wizard.getRelationshipTypeLocal().getPublicTypes();

        List<SelectItem> items = new ArrayList<SelectItem>(types.size());

        for (RelationshipType type : types) {
            items.add(new SelectItem(type.getId(), type.getName()));
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }

    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.relationships']}", String.class);
    }
}
