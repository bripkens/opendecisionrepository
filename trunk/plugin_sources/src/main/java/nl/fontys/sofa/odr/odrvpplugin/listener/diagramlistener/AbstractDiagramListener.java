/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.listener.diagramlistener;

import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramListener;
import com.vp.plugin.diagram.IDiagramListener2;
import com.vp.plugin.diagram.IDiagramUIModel;

/**
 *
 * @author Michael
 */
abstract class AbstractDiagramListener implements IDiagramListener, IDiagramListener2 {

    @Override
    public void selectionChanged(IDiagramUIModel iduim) {
    }

    /**
     * it reacts when elements are added to the diagram and prepares them for use in the odr
     * @param iduim
     * @param ide
     */
    @Override
    public void diagramElementAdded(IDiagramUIModel iduim, IDiagramElement ide) {
    }

    @Override
    public void diagramElementRemoved(IDiagramUIModel iduim, IDiagramElement ide) {
    }

    @Override
    public void diagramUIModelRenamed(IDiagramUIModel iduim) {
    }

    @Override
    public void diagramUIModelPropertyChanged(IDiagramUIModel iduim, String string, Object o, Object o1) {
    }

    @Override
    public void diagramUIModelLoaded(IDiagramUIModel iduim) {
    }
}
