package nl.rug.search.odr.controller.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.WizardStep;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.TemplateComponent;
import nl.rug.search.odr.service.AbstractWebservice;
import nl.rug.search.odr.service.Paragraph;
import nl.rug.search.odr.service.Result;
import nl.rug.search.odr.service.Webservice;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TemplateRelatedStep implements WizardStep {

    private final ManageDecisionController wizard;

    private List<TemplateComponentInput> componentValues;

    private String externalIdInput;

    private String selectedExternalId;

    private List<Result> searchResults;

    private AbstractWebservice webService;

    private Result genericInformation;

    private boolean error;

    private boolean searchError;

    public TemplateRelatedStep(ManageDecisionController wizard) {
        this.wizard = wizard;
        webService = Webservice.OPR.getInstance();
    }




    @Override
    public String getFaceletName() {
        return "decisionSteps/templateRelated.xhtml";
    }




    @Override
    public String getSidebarFaceletName() {
        return "decisionSteps/templateRelatedSidebar.xhtml";
    }




    @Override
    public void focus() {
        searchError = false;
        error = false;
        externalIdInput = null;
        selectedExternalId = null;
        searchResults = Collections.EMPTY_LIST;
        genericInformation = null;

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

        for (TemplateComponentInput value : componentValues) {
            removeValueWithComponent(d, value.getComponentValue().getComponent());
            d.addValue(value.getComponentValue());
        }
    }




    private void removeValueWithComponent(Decision decision, TemplateComponent component) {
        for (ComponentValue value : decision.getValues()) {
            if (value.getComponent().equals(component)) {
                decision.removeValue(value);
                return;
            }
        }
    }




    public List<TemplateComponentInput> getComponentValues() {
        return componentValues;
    }




    public void setComponentValues(List<TemplateComponentInput> componentValues) {
        this.componentValues = componentValues;
    }




    @Override
    public String getStepName() {
        return JsfUtil.evaluateExpressionGet("#{form['decision.wizard.headline.template']}", String.class);
    }




    public String getSelectedExternalId() {
        return selectedExternalId;
    }




    public void setSelectedExternalId(String selectedExternalId) {
        this.selectedExternalId = selectedExternalId;
    }




    




    public String getExternalIdInput() {
        return externalIdInput;
    }




    public void setExternalIdInput(String externalIdInput) {
        this.externalIdInput = externalIdInput;
    }




    public void queryWebservice() {
        search(externalIdInput);

        if (externalIdInput == null) {
            genericInformation = null;
            return;
        }

        externalIdInput = externalIdInput.trim();

        if (externalIdInput.isEmpty()) {
            genericInformation = null;
            return;
        }

        for (Result result : searchResults) {
            if (result.getName().equalsIgnoreCase(externalIdInput)) {
                genericInformation = result;
                return;
            }
        }
    }



    public List<SelectItem> getExternalPossibilities() {
        if (searchResults == null || searchResults.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<SelectItem> possibilities = new ArrayList<SelectItem>();

        for (Result searchResult : searchResults) {
            SelectItem item = new SelectItem();
            item.setLabel(searchResult.getName());
            item.setValue(searchResult.getName());
            possibilities.add(item);
        }

        return possibilities;
    }


    public void selectExternalId() {
        if (selectedExternalId == null) {
            genericInformation = null;
            return;
        }

        selectedExternalId = selectedExternalId.trim();

        if (selectedExternalId.isEmpty()) {
            genericInformation = null;
            return;
        }

        for (Result result : searchResults) {
            if (result.getName().equalsIgnoreCase(selectedExternalId)) {
                genericInformation = result;
                checkExternalInformationForErrors();
                return;
            }
        }
    }

    private void checkExternalInformationForErrors() {
        try {
            genericInformation.getDocumentation();
            error = false;
        } catch (BusinessException ex) {
            genericInformation = null;
            error = true;
        }
    }

    private void search(Object input) {
        if (input == null) {
            searchResults = Collections.emptyList();
            return;
        }

        String inputString = input.toString().trim();

        if (inputString.isEmpty()) {
            searchResults = Collections.emptyList();
            return;
        }

        error = false;

        try {
            searchResults = webService.search(inputString);
            JsfUtil.addJavascriptCall("odr.decisionWizardSearchOne(false);");
            searchError = false;
        } catch (BusinessException ex) {
            JsfUtil.addJavascriptCall("odr.decisionWizardStepOneDisableRetrieve(true);");
            searchError = true;
        }
        
    }




    public Result getGenericInformation() {
        return genericInformation;
    }




    public boolean isError() {
        return error;
    }




    public void setError(boolean error) {
        this.error = error;
    }




    public boolean isSearchError() {
        return searchError;
    }




    public void setSearchError(boolean searchError) {
        this.searchError = searchError;
    }
}
