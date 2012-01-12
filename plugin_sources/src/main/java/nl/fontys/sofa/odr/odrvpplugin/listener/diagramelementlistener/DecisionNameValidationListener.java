/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.listener.diagramelementlistener;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.model.IModelElement;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Michael
 */
public class DecisionNameValidationListener implements PropertyChangeListener {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;
    private DiagramManager diagramManager = null;
    private ViewManager viewManager = null;

    /**
     * MyPropertyChangeListener only implements propertyChange
     * if the property name of an element (decision) changes it validates
     * the name and handels error, if name is in use the name is reverted
     */
    public DecisionNameValidationListener() {
        this.diagramManager = ApplicationManager.instance().getDiagramManager();
        this.viewManager = ApplicationManager.instance().getViewManager();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        IModelElement source = (IModelElement) evt.getSource();

        //TODO remove complexity
        if (evt.getPropertyName().equals(IModelElement.PROP_NAME)) {
            if (((String) evt.getNewValue()).length() >= MIN_NAME_LENGTH
                    && ((String) evt.getNewValue()).length() <= MAX_NAME_LENGTH) {
                source.setName(((String) evt.getNewValue()).trim());
                for (IDiagramElement element : diagramManager.getActiveDiagram().toDiagramElementArray()) {
                    if (!element.getModelElement().getId().equals(source.getId())
                            && element.getModelElement().getName().equals((String) evt.getNewValue())) {
                        viewManager.showMessageDialog(viewManager.getRootFrame(), "Name already exists in diagram!");
                        source.setName(((String) evt.getOldValue()).trim());
                        source.getMasterView().getDiagramUIModel().setName(((String) evt.getOldValue()).trim());
                        return;
                    }
                }
                List<DecisionDTO> decisionDTO = ((ProjectDTO) ValueRepository.getInstance().getValue(VPStrings.PROJECT)).getDecisions();
                for (DecisionDTO decision : decisionDTO) {
                    if (decision.getName().equals(((String) evt.getNewValue()).trim())
                            && decision.getId() != Long.parseLong(Documentation.decode(source.getDocumentation()))) {
                        viewManager.showMessageDialog(viewManager.getRootFrame(), "Name already exists in the project!");
                        source.setName(((String) evt.getOldValue()).trim());
                        source.getMasterView().getDiagramUIModel().setName(((String) evt.getOldValue()).trim());
                        return;
                    }
                }
            } else {
                source.setName(((String) evt.getOldValue()).trim());
                viewManager.showMessageDialog(viewManager.getRootFrame(), "Name is too long or too short.");
            }
        }
    }
}