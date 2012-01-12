package nl.fontys.sofa.odr.odrvpplugin.controller.Views;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.diagram.IDefaultPackageDiagramUIModel;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.model.IActivityAction;
import com.vp.plugin.model.IControlFlow;
import com.vp.plugin.model.factory.IModelElementFactory;
import java.awt.Color;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.StereoTypeMapper;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.listener.diagramelementlistener.DecisionNameValidationListener;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.RelationshipDTO;

/**
 *
 * @author Administrator
 */
public abstract class BaseView {

    private IModelElementFactory factory = null;
    private IDefaultPackageDiagramUIModel diagram = null;
    private DiagramManager diagramManager = null;
    private UserSettings userSettings = null;

    /**
     * Baseclass for all Views
     * @param diagram diagram to draw on
     */
    public BaseView(IDefaultPackageDiagramUIModel diagram) {
        this.diagram = diagram;
        this.diagramManager = ApplicationManager.instance().getDiagramManager();
        this.factory = IModelElementFactory.instance();
        this.userSettings = (UserSettings) ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS);
    }

    /**
     *  creates the decision elements in the current view 
     * @param decisions decisions to display
     */
    public void createDecisions(List<DecisionDTO> decisions) {

        //iterate throug all decisions
        for (DecisionDTO decision : decisions) {
            //create diagramModel (ActivityAction) and set attributes
            IActivityAction activityAction = factory.createActivityAction();
            activityAction.setDocumentation(Documentation.encode(Long.toString(decision.getId())));
            activityAction.setName(decision.getName());

            String sType = decision.getMostRecentHistory().getState();
            activityAction.addStereotype(sType);

            //add listener
            activityAction.addPropertyChangeListener(new DecisionNameValidationListener());

            //create diagramElement and set model
            IDiagramElement displayedElement = diagramManager.createDiagramElement(diagram, activityAction);
            displayedElement.setModelElement(activityAction);

            //colorades the diagramElement
            Color color = userSettings.getDecisionStateColor(sType);
            StereoTypeMapper.setColorOfDiagramElement(displayedElement, color);
        }
    }

    /**
     * creates a ControlFlow element between two decisions
     * @param startDiagramElement DiagramElement of source
     * @param endDiagramElement DiagramElement of target
     * @param relationship relationShipDTO, empty if none
     * can be combined with createObjectFlowFlow
     * @return created controlflow as IDiagramElement or null
     */
    public IDiagramElement createControlFlow(IDiagramElement startDiagramElement, IDiagramElement endDiagramElement, RelationshipDTO relationship) {

        //TODO maybe revert to assert or throw exception
        if (startDiagramElement == null
                || endDiagramElement == null
                || startDiagramElement.getModelElement() == null
                || endDiagramElement.getModelElement() == null) {
            return null;
        }

        String relationshipType = "";
        if (relationship != null) {
            relationshipType = relationship.getRelationshipType();
        }

        // create model (controlFlow) and set attributes
        IControlFlow iControlFlow = factory.createControlFlow();
        if (relationship != null) {
            iControlFlow.setDocumentation(Documentation.encode(Long.toString(relationship.getId())));
        }
        iControlFlow.setFrom(startDiagramElement.getMetaModelElement());
        iControlFlow.setTo(endDiagramElement.getMetaModelElement());
        if (!relationshipType.isEmpty()) {
            iControlFlow.addStereotype(relationshipType);
        }

        //create diagramElement and set model
        IDiagramElement element = diagramManager.createConnector(diagram, iControlFlow, startDiagramElement, endDiagramElement, null);
        element.getModelElement().setName("");
        element.setModelElement(iControlFlow);
        element.getDiagramUIModel().setAutoFitShapesSize(true);

        return element;
    }

    /**
     * getter for DiagramElements
     * @param decisionID is used in the odr
     * @return
     */
    public IDiagramElement getDiagramElement(long decisionID) {
        for (IDiagramElement element : diagram.toDiagramElementArray()) {
            if (element.getShapeType().equals(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)) {
                String doc = Documentation.decode(element.getModelElement().getDocumentation());
                if (doc.equals(Long.toString(decisionID))) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * applies the auto layout to current diagram
     */
    public void layoutDiagram() {
        diagram.setAutoFitShapesSize(true);
        //loops through each displayed element to manually set current width and high to object
        //without it causes problems when storing diagram to vpp file and reopen it 
        for (IDiagramElement element : ApplicationManager.instance().getDiagramManager().getActiveDiagram().toDiagramElementArray()) {
            element.setSize(element.getWidth(), element.getHeight());
        }
        diagram.setAutoFitShapesSize(false);
        diagram.setAlignToGrid(true);
        diagram.setMaximized(true);
        diagramManager.autoLayout(diagram);
    }

    /**
     * getter for ModelElementFatory
     * @return ModelElementFactory
     */
    public IModelElementFactory getFactory() {
        return factory;
    }

    /**
     * setter for ModelFactory
     * @param factory ModelElementFactory
     */
    public void setFactory(IModelElementFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns the current diagram
     * @return currentDiagram
     */
    public IDefaultPackageDiagramUIModel getDiagram() {
        return diagram;
    }
}