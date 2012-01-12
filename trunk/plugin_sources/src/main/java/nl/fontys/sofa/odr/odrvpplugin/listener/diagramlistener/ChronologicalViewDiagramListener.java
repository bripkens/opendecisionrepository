/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.listener.diagramlistener;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramUIModel;
import com.vp.plugin.model.factory.IModelElementFactory;

/**
 *
 * @author Michael
 */
public class ChronologicalViewDiagramListener extends AbstractDiagramListener {

    private ViewManager viewManager = null;

    /**
     * The ChronologicalViewDiagramListener takes care of the relationship view diagram
     */
    public ChronologicalViewDiagramListener() {
        this.viewManager = ApplicationManager.instance().getViewManager();
    }

    @Override
    public void diagramElementAdded(IDiagramUIModel iduim, IDiagramElement ide) {

        if (!ide.getShapeType().equalsIgnoreCase(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)
                && !ide.getShapeType().equalsIgnoreCase(IModelElementFactory.MODEL_TYPE_CONTROL_FLOW)
                && !ide.getShapeType().equalsIgnoreCase(IModelElementFactory.MODEL_TYPE_OBJECT_NODE)) {
            viewManager.showMessageDialog(viewManager.getRootFrame(), ide.getShapeType()
                    + " is not allowed, please use "
                    + IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION
                    + " or "
                    + IModelElementFactory.MODEL_TYPE_CONTROL_FLOW
                    + " or "
                    + IModelElementFactory.MODEL_TYPE_OBJECT_NODE);
        }
    }
}
