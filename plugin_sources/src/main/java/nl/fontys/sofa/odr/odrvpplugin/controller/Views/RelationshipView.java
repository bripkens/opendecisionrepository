package nl.fontys.sofa.odr.odrvpplugin.controller.Views;

import com.vp.plugin.diagram.IDefaultPackageDiagramUIModel;
import com.vp.plugin.diagram.IDiagramElement;
import java.util.ArrayList;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.listener.diagramlistener.RelationshipViewDiagramListener;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.RelationshipDTO;

/**
 *
 * @author Administrator
 */
public class RelationshipView extends BaseView {

    private RelationshipViewDiagramListener listener = null;
    private List<DecisionDTO> decisions = null;

    /**
     * Create the relationshipView
     * @param diagram the diagram to draw on
     */
    public RelationshipView(IDefaultPackageDiagramUIModel diagram) {
        super(diagram);
        this.listener = new RelationshipViewDiagramListener();
        this.decisions = new ArrayList<DecisionDTO>();
    }

    /**
     * draws the relationship view
     * @param decisions decision to draw
     */
    public void drawDiagram(List<DecisionDTO> decisions) {
        this.decisions = decisions;
        createDecisions(decisions);
        createRelationships();
        addDiagramListener();
    }

    /**
     * updates an existing diagram with passed decisions
     * @param decisions decsion to draw additionly
     */
    public void updateDiagram(List<DecisionDTO> decisions) {
        this.decisions = decisions;
        removeDiagramListener();
        createDecisions(findNewDecisions());
        createRelationships();
        addDiagramListener();
    }

    /*
     * draws all relationships in the current diagram
     */
    private void createRelationships() {

        //iterate through all decisions
        for (DecisionDTO decision : decisions) {
            long fromId = decision.getId();
            IDiagramElement startDiagramElement = getDiagramElement(fromId);

            for (RelationshipDTO relation : decision.getRelationships()) {

                long toId = relation.getTargetId();
                IDiagramElement endDiagramElement = getDiagramElement(toId);

                //create controlflow between elements
                createControlFlow(startDiagramElement, endDiagramElement, relation);
            }
        }
    }

    /*
     * filters the currently stored list of decisions (getDecisions)
     * and returns a list of not yet displayed decisions
     */
    private List<DecisionDTO> findNewDecisions() {
        List<DecisionDTO> newDecisions = new ArrayList<DecisionDTO>();

        for (DecisionDTO decision : decisions) {
            //get DiagramElement by id
            IDiagramElement element = getDiagramElement(decision.getId());
            if (element == null) {
                //if element is null, the element doesn't exist in the current diagram
                newDecisions.add(decision);
            }
        }
        return newDecisions;
    }

    /*
     * adds the diagramListener
     */
    private void addDiagramListener() {
        getDiagram().addDiagramListener(listener);
    }

    /*
     * removes the diagramListener
     */
    private void removeDiagramListener() {
        getDiagram().removeDiagramListener(listener);
    }
}