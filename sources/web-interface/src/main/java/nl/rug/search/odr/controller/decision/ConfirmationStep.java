package nl.rug.search.odr.controller.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.Requirement;
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


    public List<ComponentValue> getComponentValues() {
        return new ArrayList<ComponentValue>(wizard.getDecision().getValues());
    }

    public List<Requirement> getRequirements() {
        return new ArrayList<Requirement>(wizard.getVersion().getRequirements());
    }

    public List<ProjectMember> getInitiators() {
        return new ArrayList<ProjectMember>(wizard.getVersion().getInitiators());
    }

    public List<Dto> getRelationships() {
        Collection<Relationship> relationships = wizard.getVersion().getRelationships();
        List<Dto> dtos = new ArrayList<Dto>();

        for(Relationship eachRelationship : relationships) {
            Dto dto = new Dto();
            dto.setTargetName(wizard.getDecisionLocal().getByVersion(eachRelationship.getTarget().getId()).getName());
            dto.setTypeName(eachRelationship.getType().getName());

            dtos.add(dto);
        }

        return dtos;
    }

    public static class Dto {
        private String typeName;
        private String targetName;






        public String getTargetName() {
            return targetName;
        }




        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }




        public String getTypeName() {
            return typeName;
        }




        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        
    }
}
