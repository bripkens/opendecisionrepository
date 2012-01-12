/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller.Views;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import nl.fontys.sofa.odr.odrvpplugin.listener.diagramlistener.StackholderInvolvementViewDiagramListener;
import com.vp.plugin.diagram.IDefaultPackageDiagramUIModel;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramListener;
import com.vp.plugin.diagram.IShapeUIModel;
import com.vp.plugin.model.IActor;
import com.vp.plugin.model.ISystem;
import com.vp.plugin.model.IUseCase;
import java.util.ArrayList;
import java.util.List;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;

/**
 *
 * @author Administrator
 */
public class StakeholderInvolvementView extends BaseView {

    private DiagramManager diagramManager = ApplicationManager.instance().getDiagramManager();
    private IDiagramListener listener = null;
    private List<DecisionDTO> decisions = null;

    /**
     * Create the stakeholderInvolvementView
     * @param diagram the diagram to draw on
     */
    public StakeholderInvolvementView(IDefaultPackageDiagramUIModel diagram) {
        super(diagram);
        listener = new StackholderInvolvementViewDiagramListener();
        this.decisions = new ArrayList<DecisionDTO>();
    }

    /**
     * draws the stakeholderInvolvement view
     * @param decisions decision to draw
     */
    public void drawDiagram(List<DecisionDTO> decisions) {
        this.decisions = decisions;

        for (DecisionDTO d : decisions) {
            IDiagramElement system = system(d);
            decision(d, system);
            maennchen(d);
        }

        addDiagramListener();
    }

    private void maennchen(DecisionDTO d) {
        HistoryDTO history = d.getHistory().get(0);
        for (String initiator : history.getInitiators()) {
            IActor actor = getFactory().createActor();
            actor.setName(initiator);
            actor.addStereotype("irgendwas");
            IDiagramElement displayedElement = diagramManager.createDiagramElement(getDiagram(), actor);
            displayedElement.setModelElement(actor);
        }
        decisions.size();
    }

    private void decision(DecisionDTO d, IDiagramElement system) {
        IUseCase useCase = getFactory().createUseCase();
        useCase.setName(d.getName());
        IDiagramElement displayedElement = diagramManager.createDiagramElement(getDiagram(), useCase);

        displayedElement.setModelElement(useCase);
        system.getModelElement().addChild(useCase);
        system.addChild((IShapeUIModel) displayedElement);

    }

    private IDiagramElement system(DecisionDTO d) {
        ISystem system = getFactory().createSystem();
        system.setName(d.getName());
        IDiagramElement displayedElement = diagramManager.createDiagramElement(getDiagram(), system);
        displayedElement.setModelElement(system);
        return displayedElement;
    }

    private void addDiagramListener() {
        getDiagram().addDiagramListener(listener);
    }
}
