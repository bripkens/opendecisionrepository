/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.listener.diagramlistener;

import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramUIModel;

/**
 *
 * @author Michael
 */
public class StackholderInvolvementViewDiagramListener extends AbstractDiagramListener {

    private String msg = "Not supported yet.";

    public StackholderInvolvementViewDiagramListener() {
    }

    @Override
    public void diagramElementAdded(IDiagramUIModel iduim, IDiagramElement ide) {
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void diagramElementRemoved(IDiagramUIModel iduim, IDiagramElement ide) {
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void diagramUIModelRenamed(IDiagramUIModel iduim) {
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void diagramUIModelPropertyChanged(IDiagramUIModel iduim, String string, Object o, Object o1) {
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void diagramUIModelLoaded(IDiagramUIModel iduim) {
        throw new UnsupportedOperationException(msg);
    }
}
