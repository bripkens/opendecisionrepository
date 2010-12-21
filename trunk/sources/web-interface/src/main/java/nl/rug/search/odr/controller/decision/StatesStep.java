package nl.rug.search.odr.controller.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import nl.rug.search.odr.SelectItemComparator;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StatesStep implements WizardStep {

    private final ManageDecisionController wizard;

    private List<State> states;

    private State state;

    private List<ProjectMember> initiators;

    private String selectedInitiator;

    private Date decidedWhen;


    public StatesStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/states.xhtml";
    }




    @Override
    public String getSidebarFaceletName() {
        return "decisionSteps/statesSidebar.xhtml";
    }




    @Override
    public void focus() {
        selectedInitiator = null;
        initiators = new ArrayList<ProjectMember>(wizard.getVersion().getInitiators());
        states = wizard.getStateLocal().getCommonStates();
        state = wizard.getVersion().getState();
        decidedWhen = wizard.getVersion().getDecidedWhen();
    }




    @Override
    public void blur() {
        Version version = wizard.getVersion();

        version.removeAllInitiators();
        version.setInitiators(initiators);
        version.setState(state);
        version.setDecidedWhen(decidedWhen);
    }




    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.state']}", String.class);
    }




    public List<ProjectMember> getInitiators() {
        return initiators;
    }




    public List<SelectItem> getStates() {
        List<SelectItem> items = new ArrayList<SelectItem>(states.size());

        for (State state : states) {
            items.add(new SelectItem(state.getId(), state.getStatusName()));
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }




    public void setState(final String stateId) {
        long stateIdLong = Long.parseLong(stateId);

        for (State state : states) {
            if (state.getId().equals(stateIdLong)) {
                this.state = state;
                return;
            }
        }
    }




    public String getState() {
        if (state == null) {
            return null;
        }

        return state.getId().toString();
    }



    public Date getDecidedWhen() {
        return decidedWhen;
    }




    public void setDecidedWhen(Date decidedWhen) {
        this.decidedWhen = decidedWhen;
    }



    public List<SelectItem> getAvailableInitiators() {
        Collection<ProjectMember> allMembers = wizard.getProject().getMembers();
        List<SelectItem> items = new ArrayList<SelectItem>(allMembers.size());

        for (ProjectMember eachMember : allMembers) {
            if (initiators.contains(eachMember)) {
                continue;
            }

            String label = eachMember.getPerson().getName();

            if (!StringValidator.isValid(label, false)) {
                label = eachMember.getPerson().getEmail();
            }

            items.add(new SelectItem(eachMember.getId(), label));
        }

        Collections.sort(items, new SelectItemComparator());

        return items;
    }




    public void addInitiator() {
        if (!StringValidator.isValid(selectedInitiator, false)
            || selectedInitiator.equalsIgnoreCase(
                JsfUtil.evaluateExpressionGet("#{form['label.pleaseSelect']}", String.class))) {
            return;
        }

        long projectMemberId = Long.parseLong(selectedInitiator);

        for (ProjectMember eachMember : wizard.getProject().getMembers()) {
            if (eachMember.getId().equals(projectMemberId)) {
                initiators.add(eachMember);
                selectedInitiator = null;
                return;
            }
        }
    }




    public void removeInitiator(long initiatorId) {
        if (initiators.size() == 1) {
            FacesContext.getCurrentInstance().addMessage("manageDecisionForm",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    JsfUtil.evaluateExpressionGet("#{form['decision.wizard.state.initiator.required']}", String.class),
                    null));
            return;
        }

        for (ProjectMember eachMember : initiators) {
            if (eachMember.getId().equals(initiatorId)) {
                initiators.remove(eachMember);
                return;
            }
        }
    }




    public String getSelectedInitiator() {
        return selectedInitiator;
    }




    public void setSelectedInitiator(String selectedInitiator) {
        this.selectedInitiator = selectedInitiator;
    }
}
