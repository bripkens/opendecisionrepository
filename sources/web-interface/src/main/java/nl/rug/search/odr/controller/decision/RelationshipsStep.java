package nl.rug.search.odr.controller.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import nl.rug.search.odr.SelectItemComparator;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.State;
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

    private String selectedDecisionId;

    private String decisionName;
    private State initialState;
    private State selectedState;
    private Collection<State> states;

    public RelationshipsStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/relationships.xhtml";
    }

    @Override
    public String getSidebarFaceletName() {
        return "decisionSteps/relationshipsSidebar.xhtml";
    }


    @Override
    public void focus() {
        selectedDecisionId = null;

        types = wizard.getRelationshipTypeLocal().getPublicTypes();

        relationships = new ArrayList<RelationshipStepInput>();
        Version version = wizard.getVersion();

        for (Relationship relationship : version.getOutgoingRelationships()) {
            Version target = relationship.getTarget();
            Decision targetDecision = target.getDecision();

            RelationshipStepInput input = new RelationshipStepInput(relationship,
                    targetDecision,
                    target.getId().toString(),
                    relationship.getType().getId().toString(),
                    wizard.getVersion().getDecidedWhen());

            relationships.add(input);
        }

        initialState = wizard.getStateLocal().getInitialState();
        selectedState = initialState;
        states = wizard.getStateLocal().getCommonStates();
    }




    @Override
    public void blur() {
        Version version = wizard.getVersion();

        for (RelationshipStepInput input : relationships) {
            RelationshipType type = getRelationshipType(Long.parseLong(input.getType()));
            Version target = input.getDecision().getVersion(Long.parseLong(input.getVersion()));

            Relationship relationship = input.getRelationship();
            relationship.setType(type);
            relationship.setTarget(target);

            Collection<Relationship> existingRelationships = version.getOutgoingRelationships();

            if (!existingRelationships.contains(relationship)) {
                version.addOutgoingRelationship(relationship);
            }
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




    public void addRelationship(String value) {
        if (!StringValidator.isValid(value, false)) {
            return;
        } else if (value.equalsIgnoreCase(
                JsfUtil.evaluateExpressionGet("#{form['label.pleaseSelect']}", String.class))) {
            return;
        }

        long id = Long.parseLong(value);

        for (Decision decision : wizard.getProject().getDecisions()) {
            if (decision.getId().equals(id)) {
                RelationshipStepInput relationship = new RelationshipStepInput(null,
                        decision,
                        null,
                        null,
                        wizard.getVersion().getDecidedWhen());
                relationships.add(0, relationship);
                selectedDecisionId = null;
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
                wizard.getVersion().removeOutgoingRelationship(relationship.getRelationship());
                versionSelectionChanged(null);
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




    public String getSelectedDecisionId() {
        return selectedDecisionId;
    }




    public void setSelectedDecisionId(String selectedDecisionId) {
        this.selectedDecisionId = selectedDecisionId;
    }




    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.relationships']}", String.class);
    }




    public void versionSelectionChanged(ValueChangeEvent e) {
        Version decisionVersion = wizard.getVersion();

        for (RelationshipStepInput input : relationships) {
            Version target;

            if (e != null && input.getVersion().equalsIgnoreCase(e.getOldValue().toString())) {
                target = input.getDecision().getVersion(Long.parseLong(e.getNewValue().toString()));
            } else {
                target = input.getDecision().getVersion(Long.parseLong(input.getVersion()));
            }

            if (decisionVersion.getDecidedWhen().before(target.getDecidedWhen())) {
                JsfUtil.addJavascriptCall("odr.toggling.slideDown('#decisionAfterInformation');");
                return;
            }
        }

        JsfUtil.addJavascriptCall("j('#decisionAfterInformation').slideUp();");
    }



    public List<SelectItem> getStates() {
        List<SelectItem> items = new ArrayList<SelectItem>(states.size());

        for (State state : states) {
            items.add(new SelectItem(state.getId(), state.getStatusName()));
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }




    public String getState() {
        return selectedState.getId().toString();
    }





    public void setState(String stateString) {
        long stateId = Long.parseLong(stateString);

        for (State state : states) {
            if (state.getId().equals(stateId)) {
                this.selectedState = state;
                return;
            }
        }
    }




    public String getDecisionName() {
        return decisionName;
    }




    public void setDecisionName(String decisionName) {
        this.decisionName = decisionName;
    }






    public void addDecision() {
        Decision d = new Decision();
        d.setName(decisionName);
        d.setTemplate(wizard.getDecisionTemplateLocal().getSmallestTemplate());
        
        Version initialVersion = new Version();
        Date currentdate = new Date();
        initialVersion.setDecidedWhen(currentdate);
        initialVersion.setDocumentedWhen(currentdate);
        initialVersion.setState(selectedState);
        initialVersion.addInitiator(wizard.getMember());

        d.addVersion(initialVersion);

        wizard.getDecisionLocal().persist(d);

        Project p = wizard.getProjectLocal().getById(wizard.getProject().getId());
        p.addDecision(d);

        wizard.getProjectLocal().merge(p);

        wizard.getProject().addDecision(d);

        cancelAddDecision();

        addRelationship(d.getId() + "");
    }




    public void cancelAddDecision() {
        selectedState = initialState;
        decisionName = null;

        JsfUtil.addJavascriptCall("j('#wizardQuickAddDecision').dialog('close');;");
    }
}
