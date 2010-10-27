package nl.rug.search.odr.controller.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.TemplateComponent;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TemplateRelatedStep implements WizardStep {

    private final ManageDecisionController wizard;

    private List<TemplateComponentInput> componentValues;




    public TemplateRelatedStep(ManageDecisionController wizard) {
        this.wizard = wizard;
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/templateRelated.xhtml";
    }




    @Override
    public void focus() {
        Collection<TemplateComponent> components = wizard.getDecision().getTemplate().getComponents();
        Collection<ComponentValue> values = wizard.getDecision().getValues();

        componentValues = new ArrayList<TemplateComponentInput>(components.size());



        Iterator<TemplateComponent> componentsIterator = components.iterator();
        while (componentsIterator.hasNext()) {
            TemplateComponent component = componentsIterator.next();

            boolean contained = false;

            for (ComponentValue value : values) {
                if (value.getComponent().equals(component)) {
                    contained = true;
                    break;
                }
            }

            if (!contained) {
                boolean last = !componentsIterator.hasNext() && values.isEmpty();
                componentValues.add(new TemplateComponentInput(component, last));
            }
        }

        Iterator<ComponentValue> valuesIterator = values.iterator();
        while (valuesIterator.hasNext()) {
            ComponentValue value = valuesIterator.next();
            if (components.contains(value.getComponent())) {
                componentValues.add(new TemplateComponentInput(value.getComponent(), value, !valuesIterator.hasNext()));
            }
        }

        Collections.sort(componentValues, new TemplateComponentInput.OrderComparator());
    }




    @Override
    public void blur() {
        Decision d = wizard.getDecision();
        d.removeAllValues();

        for (TemplateComponentInput value : componentValues) {
            d.addValue(value.getComponentValue());
        }
    }




    public List<TemplateComponentInput> getComponentValues() {
        return componentValues;
    }




    public void setComponentValues(List<TemplateComponentInput> componentValues) {
        this.componentValues = componentValues;
    }
}
