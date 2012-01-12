/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.listener.diagramlistener;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramUIModel;
import com.vp.plugin.model.IActivityAction;
import com.vp.plugin.model.IControlFlow;
import com.vp.plugin.model.factory.IModelElementFactory;
import java.awt.Color;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.StereoTypeMapper;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.listener.diagramelementlistener.DecisionNameValidationListener;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;

/**
 *
 * @author Michael
 */
public class RelationshipViewDiagramListener extends AbstractDiagramListener {

    private ViewManager viewManager = null;

    /**
     * The RelationshipViewDiagramListener takes care of the relationship view diagram
     */
    public RelationshipViewDiagramListener() {
        this.viewManager = ApplicationManager.instance().getViewManager();
    }

    @Override
    public void diagramElementAdded(IDiagramUIModel iduim, IDiagramElement ide) {
        if (ide.getShapeType().equalsIgnoreCase(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)) {
            IActivityAction action = (IActivityAction) ide.getModelElement();
            action.addPropertyChangeListener(new DecisionNameValidationListener());

            String stereoType = "";
            List<DecisionStateDTO> decisionstates = (List<DecisionStateDTO>) ((UserSettings) ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS)).getDecisionStates();
            for (DecisionStateDTO dec : decisionstates) {
                if (dec.isInitialState()) {
                    stereoType = dec.getName();
                    break;
                }
            }

            UserSettings userSettings = (UserSettings) ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS);
            Color color = userSettings.getDecisionStateColor(stereoType);
            StereoTypeMapper.setColorOfDiagramElement(ide, color);

            action.addStereotype(stereoType);
        } else if (ide.getShapeType().equalsIgnoreCase(IModelElementFactory.MODEL_TYPE_CONTROL_FLOW)) {
            IControlFlow flow = (IControlFlow) ide.getModelElement();
            //TODO
            flow.addStereotype("caused by");
        } else {
            //TODO
            viewManager.showMessageDialog(viewManager.getRootFrame(), ide.getShapeType()
                    + " is not allowed and will be ignored, please use "
                    + IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION
                    + " or "
                    + IModelElementFactory.MODEL_TYPE_CONTROL_FLOW
                    + "for working with odr");
        }
    }
}