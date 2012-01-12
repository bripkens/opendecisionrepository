/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.listener.diagramelementlistener;

import com.vp.plugin.diagram.IConnectorUIModel;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramElementListener;
import com.vp.plugin.diagram.IShapeUIModel;

/**
 *
 * @author Michael
 */
abstract class AbstractDiagramElementListener implements IDiagramElementListener {

    @Override
    public void childAdded(IDiagramElement ide, IShapeUIModel isuim) {
    }

    @Override
    public void childRemoved(IDiagramElement ide, IShapeUIModel isuim) {
    }

    @Override
    public void propertyUpdated(IDiagramElement ide) {
    }

    @Override
    public void nameUpdated(IDiagramElement ide) {
    }

    @Override
    public void diagramElementPropertyChange(IDiagramElement ide, String string) {
    }

    @Override
    public void diagramElementUndeleted(IDiagramElement ide) {
    }

    @Override
    public void fromConnectorAdded(IDiagramElement ide, IConnectorUIModel icuim) {
    }

    @Override
    public void fromConnectorRemoved(IDiagramElement ide, IConnectorUIModel icuim) {
    }

    @Override
    public void toConnectorAdded(IDiagramElement ide, IConnectorUIModel icuim) {
    }

    @Override
    public void toConnectorRemoved(IDiagramElement ide, IConnectorUIModel icuim) {
    }
}