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
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipsStep implements WizardStep {

    private final ManageDecisionController wizard;

    private List<RelationshipStepInput> relationships;

    private List<RelationshipType> types;




    public RelationshipsStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/relationships.xhtml";
    }




    @Override
    public void focus() {
        types = wizard.getRelationshipTypeLocal().getPublicTypes();

        relationships = new ArrayList<RelationshipStepInput>();
        Version version = wizard.getVersion();
        DecisionLocal dl = wizard.getDecisionLocal();

        for (Relationship relationship : version.getRelationships()) {
            Version target = relationship.getTarget();
            Decision targetDecision = dl.getByVersion(target.getId());

            RelationshipStepInput input = new RelationshipStepInput(relationship,
                    targetDecision,
                    target.getId().toString(),
                    relationship.getType().getId().toString(),
                    wizard.getVersion().getDecidedWhen());

            relationships.add(input);
        }
    }




    @Override
    public void blur() {
        Version version = wizard.getVersion();
        version.removeAllRelationships();


        for (RelationshipStepInput input : relationships) {
            RelationshipType type = getRelationshipType(Long.parseLong(input.getType()));
            Version target = input.getDecision().getVersion(Long.parseLong(input.getVersion()));

            Relationship relationship = input.getRelationship();
            relationship.setType(type);
            relationship.setTarget(target);

            version.addRelationship(relationship);
        }
    }




    private RelationshipType getRelationshipType(long id) {
        for (RelationshipType type : types) {
            if (type.getId().equals(id)) {
                return type;
            }
        }

        throw new RuntimeException("Invalid relationship type id");
    }




    public void decisionSelectionChangeListener(ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (!StringValidator.isValid(value, false)) {
            return;
        } else if (value.equalsIgnoreCase(
                JsfUtil.evaluateExpressionGet("#{form['label.pleaseSelect']}", String.class))) {
            return;
        }

        long id = Long.parseLong(e.getNewValue().toString());

        for (Decision decision : wizard.getProject().getDecisions()) {
            if (decision.getId().equals(id)) {
                RelationshipStepInput relationship = new RelationshipStepInput(null,
                        decision,
                        null,
                        null,
                        wizard.getVersion().getDecidedWhen());
                relationships.add(relationship);
                return;
            }
        }
    }




    public List<SelectItem> getAvailableDecisions() {
        Collection<Decision> allDecisions = wizard.getProject().getDecisions();

        List<SelectItem> items = new ArrayList<SelectItem>(allDecisions.size());

        String afterMessage = JsfUtil.evaluateExpressionGet("#{form['decision.wizard.decided.after']}", String.class);

        for (Decision decision : allDecisions) {
            if (decision.getId().equals(wizard.getDecision().getId())) {
                continue;
            } else if (decision.isRemoved()) {
                continue;
            } else if (relationshipContainsDecision(decision)) {
                continue;
            }

            String label = decision.getName();
            if (decision.getFirstVersion().getDecidedWhen().after(wizard.getVersion().getDecidedWhen())) {
                label = label.concat(" ").concat(afterMessage);
            }

            SelectItem item;
            item = new SelectItem(decision.getId(), label);
            items.add(item);
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }




    private boolean relationshipContainsDecision(Decision decision) {
        for (RelationshipStepInput relationship : relationships) {
            if (relationship.getDecision().equals(decision)) {
                return true;
            }
        }

        return false;
    }




    public List<RelationshipStepInput> getRelationships() {
        return relationships;
    }




    public void removeRelationship(long decisionId) {
        for (RelationshipStepInput relationship : relationships) {
            if (relationship.getDecision().getId().equals(decisionId)) {
                relationships.remove(relationship);
                return;
            }
        }
    }




    public List<SelectItem> getRelationshipTypes() {
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
