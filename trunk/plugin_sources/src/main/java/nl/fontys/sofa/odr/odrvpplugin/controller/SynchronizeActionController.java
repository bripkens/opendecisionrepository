package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramUIModel;
import com.vp.plugin.model.IActivityAction;
import com.vp.plugin.model.IControlFlow;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.factory.IModelElementFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import nl.fontys.sofa.odr.odrvpplugin.common.DiagramProperties;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.Serializer;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.controller.helper.DecisionDTOWrapper;
import nl.fontys.sofa.odr.odrvpplugin.factory.DiagramFacade;
import nl.rug.search.odr.ws.connection.AuthorizedWebServiceFacade;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;
import nl.rug.search.odr.ws.dto.EditDecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;
import nl.rug.search.odr.ws.dto.RelationshipDTO;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;

/**
 *
 * @author Michael
 */
public class SynchronizeActionController implements VPActionController {

    private long myId = 0L;
    private ViewManager viewManager = null;
    private DiagramManager diagramManager = null;
    private ProjectDTO projectDTO = null;
    private List<DecisionDTOWrapper> decisions = null;
    private IDiagramUIModel currentDiagram = null;
    private int errors = 0;
    private UserSettings userSettings = null;

    /**
     * 
     * @param action
     */
    @Override
    public void performAction(VPAction action) {
        ValueRepository vr = ValueRepository.getInstance();
        ApplicationManager am = ApplicationManager.instance();

        viewManager = am.getViewManager();
        diagramManager = am.getDiagramManager();

        userSettings = (UserSettings) vr.getValue(VPStrings.USERSETTINGS);
        projectDTO = (ProjectDTO) vr.getValue(VPStrings.PROJECT);

        decisions = getDecisionWrapper(projectDTO);
        currentDiagram = diagramManager.getActiveDiagram();

        String serializedObj =
                Documentation.decode(currentDiagram.getDocumentation());


        

        DiagramProperties props =
                Serializer.decode(serializedObj, DiagramProperties.class);

       if(props==null){
           viewManager.showMessageDialog(viewManager.getRootFrame(), "only odr diagram types can be synchronized");
           return;
       }
        if (props.getDiagramType().equals(DiagramFacade.RELATIONSHIPVIEW)) {
            errors = 0;

            updateProjectDTO();
            //in case some displayed decisions have no idea althoug they should
            updateDecisionsWithNewId();

            locatedChangesInDecisions();
            //sync located changed
            syncWithODR();

            //update prjDTO to get updated id's
            updateProjectDTO();
            updateDecisionsWithNewId();

            locateChangesInRelationships();
            //sync located changes
            syncWithODR();

            //update prjDTO to get updated id's
            updateProjectDTO();
            updateRelationshipWithNewId();

            if (errors == 0) {
                viewManager.showMessageDialog(viewManager.getRootFrame(),
                        "successfully synchronized changes");
            } else {
                viewManager.showMessageDialog(viewManager.getRootFrame(),
                        "there where " + errors + " during synchronization");
            }
        } else {
            viewManager.showMessageDialog(new JLabel(),
                    "This function is not supported by " + props.getDiagramType(),
                    "Unsupported Function", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 
     * @param action
     */
    @Override
    public void update(VPAction action) {
    }

    /**
     * return a diagrambase negativ unique id
     * negative id's are used to idetify manually (new) added decisions in
     * diagrams
     */
    private long getMyId() {
        //decrease by 1 and return internal id 
        return --myId;
    }

    //TODO document
    private int locatedChangesInDecisions() {
        int counter = 0;
        for (IDiagramElement element :
                currentDiagram.toDiagramElementArray(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)) {
            IActivityAction model = (IActivityAction) element.getModelElement();
            String state = getDecisionStateByStereoType(model);

            //new decision found
            if (model.getDocumentation().isEmpty()) {
                DecisionDTOWrapper newDecision = new DecisionDTOWrapper();

                HistoryDTO history = new HistoryDTO();
                history.setDecidedWhen(new Date());
                history.setDocumentedWhen(new Date());
                history.setState(state);
                List<HistoryDTO> historyList = new ArrayList<HistoryDTO>();
                historyList.add(history);
                newDecision.setHistory(historyList);

                newDecision.setId(getMyId());
                newDecision.setName(model.getName());
                newDecision.setRelationships(new ArrayList<RelationshipDTO>());
                newDecision.setModified(true);
                decisions.add(newDecision);
                counter++;
            } else {
                //decision existed before -> check for changes
                String doc = element.getModelElement().getDocumentation();
                String odrId = Documentation.decode(doc);
                DecisionDTOWrapper decision =
                        getDecisionWrapperById(Long.parseLong(odrId));

                String decisionId = Documentation.decode(model.getDocumentation());
                decision.setId(Long.parseLong(decisionId));
                decision.setRelationships(decision.getRelationships());
                if (!decision.getName().equals(model.getName())
                        || !decision.getMostRecentHistory().getState().equals(state)) {
                    decision.setName(model.getName());
                    HistoryDTO history = new HistoryDTO();
                    history.setDecidedWhen(new Date());
                    history.setDocumentedWhen(new Date());
                    history.setState(state);
                    decision.getHistory().add(history);
                    decision.setModified(true);
                    counter++;
                }
            }
        }
        return counter;
    }

    private int syncWithODR() {
        int counter = 0;
        Long projectID = projectDTO.getId();
        AuthorizedWebServiceFacade authorizedWebServiceFacade =
                getAuthorizedWebServiceFacade();
        for (DecisionDTOWrapper wrapper : decisions) {
            if (wrapper.isModified()) {
                EditDecisionDTO dto = wrapper.toEditDecisionDTO();
                if (dto.getId() > 0) {
                    try {
                        //infoAboutDTO(dto);
                        authorizedWebServiceFacade.updateDecision(projectID, dto);
                        counter++;
                    } catch (IOException ex) {
                        Logger.getLogger(SynchronizeActionController.class.getName()).log(Level.SEVERE, null, ex);
                        viewManager.showMessageDialog(viewManager.getRootFrame(),
                                "Error while synchronizing (update) " + ex.getMessage());
                        errors++;
                    }
                } else {
                    try {
                        viewManager.showMessage("create " + dto.getName());
                        authorizedWebServiceFacade.addDecision(projectID, dto);
                        counter++;
                    } catch (Exception ex) {
                        Logger.getLogger(SynchronizeActionController.class.getName()).log(Level.SEVERE, null, ex);
                        viewManager.showMessageDialog(viewManager.getRootFrame(),
                                "Error while synchronizing (add)" + ex.getMessage());
                        errors++;
                    }
                }
            }
        }
        return counter;
    }

    //TODO document
    private int locateChangesInRelationships() {
        int counter = 0;
        for (IDiagramElement decisionElement :
                currentDiagram.toDiagramElementArray(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)) {
            DecisionDTOWrapper decision =
                    getDecisionWrapperById(getIdByDiagramElement(decisionElement));

            for (IControlFlow flow : findRelationshipsWithStartPointAt(decisionElement)) {
                String relationshipId = Documentation.decode(flow.getDocumentation());
                Long targetId = getIdByModelElement(flow.getTo());
                if (relationshipId.isEmpty()) {
                    //new relationship
                    RelationshipDTO relationshipDTO = new RelationshipDTO();

                    relationshipDTO.setRelationshipType(getRelationshipTypeByStereoType(flow));
                    relationshipDTO.setTargetId(targetId);
                    decision.getRelationships().add(relationshipDTO);
                    decision.setModified(true);
                    counter++;
                } else {
                    //existing relationship
                    for (RelationshipDTO relationshipDTO : decision.getRelationships()) {
                        if (relationshipDTO.getId() == Long.parseLong(relationshipId)) {
                            String relationshipType = getRelationshipTypeByStereoType(flow);
                            if (!relationshipDTO.getRelationshipType().equals(relationshipType)) {
                                relationshipDTO.setRelationshipType(relationshipType);
                                decision.setModified(true);
                                counter++;
                            }
                            if (relationshipDTO.getTargetId() != targetId) {
                                relationshipDTO.setTargetId(targetId);
                                decision.setModified(true);
                                counter++;
                            }
                        }
                    }
                }
            }
        }
        return counter;
    }

    /**
     * refreshes the projectDTO (and decisions) from the webservice
     */
    private void updateProjectDTO() {
        ProjectDTO project;
        try {
            project = getAuthorizedWebServiceFacade().getProject(projectDTO.getId());
            ValueRepository.getInstance().setValue(VPStrings.PROJECT, project);
            projectDTO = project;
            decisions = getDecisionWrapper(projectDTO);
        } catch (IOException ex) {
            Logger.getLogger(SynchronizeActionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * return a Webserviceconnection
     * @return AuthorizedWebServiceFacade
     */
    private AuthorizedWebServiceFacade getAuthorizedWebServiceFacade() {
        String connectionString = userSettings.getUrl();
        String email = userSettings.getUsername();
        String password = userSettings.getEncryptedPassword();
        return new AuthorizedWebServiceFacade(connectionString, email, password);
    }

    /**
     * returns a specific decisionDTOWrapper by Id
     * @param fromId id
     * @return DecisionDTOWrapper
     */
    private DecisionDTOWrapper getDecisionWrapperById(Long fromId) {
        for (DecisionDTOWrapper dec : decisions) {
            if (dec.getId() == fromId) {
                return dec;
            }
        }
        throw new IllegalStateException();
    }

    /**
     * returns models first valid sterotype, in case no valid
     * sterotype is found the default sterotype defined by the usersettings
     * is choosen, in case none is defined an exeption is thrown
     * @param model to read the sterotype from
     * @return a sterotype
     */
    private String getDecisionStateByStereoType(IActivityAction model) {
        List<DecisionStateDTO> decisionStates = userSettings.getDecisionStates();
        for (String type : model.toStereotypeArray()) {
            Iterator<DecisionStateDTO> it = decisionStates.iterator();
            while (it.hasNext()) {
                if (it.next().getName().equalsIgnoreCase(type)) {
                    return type;
                }
            }
        }
        //in case no valid type is found the default type is returned
        for (DecisionStateDTO dto : decisionStates) {
            if (dto.isCommon()) {
                return dto.getName();
            }
        }
        throw new IllegalStateException("no default state found");
    }

    /**
     * returns models first valid relationshiptype, in case no valid
     * relationshiptype is found the default relationshiptype defined by the usersettings
     * is choosen, in case none is defined an exeption is thrown
     * @param model to read the relationshiptype from
     * @return a relationshiptype
     */
    private String getRelationshipTypeByStereoType(IControlFlow model) {
        List<RelationshipTypeDTO> relationshipTypes =
                userSettings.getRelationshipTypes();
        for (String type : model.toStereotypeArray()) {
            Iterator<RelationshipTypeDTO> it = relationshipTypes.iterator();
            while (it.hasNext()) {
                if (it.next().getName().equalsIgnoreCase(type)) {
                    return type;
                }
            }
        }
        //in case no valid type is found the default type is returned
        for (RelationshipTypeDTO dto : relationshipTypes) {
            if (dto.isCommon()) {
                return dto.getName();
            }
        }
        throw new IllegalStateException("no default type found");
    }

    /**
     * updates all decision with it's concrete odr id
     */
    private int updateDecisionsWithNewId() {
        int counter = 0;
        for (IDiagramElement element :
                currentDiagram.toDiagramElementArray(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)) {
            if (element.getModelElement().getDocumentation().isEmpty()) {
                for (DecisionDTO decision : decisions) {
                    if (decision.getName().equals(element.getModelElement().getName())) {
                        String documentation = Documentation.encode(decision.getId() + "");
                        element.getModelElement().setDocumentation(documentation);
                        counter++;
                        break;
                    }
                }
            }
        }
        return counter;
    }

    /**
     * updates all relationships with its concrete odr id
     */
    private int updateRelationshipWithNewId() {
        int counter = 0;
        for (IDiagramElement element :
                currentDiagram.toDiagramElementArray(IModelElementFactory.MODEL_TYPE_CONTROL_FLOW)) {
            if (element.getModelElement().getDocumentation().isEmpty()) {
                for (DecisionDTO decision : decisions) {
                    IModelElement source = ((IControlFlow) element.getModelElement()).getFrom();
                    IModelElement target = ((IControlFlow) element.getModelElement()).getTo();
                    if (decision.getName().equals(source.getName())) {
                        for (RelationshipDTO rel : decision.getRelationships()) {
                            String targetId = Documentation.decode(target.getDocumentation());
                            if (rel.getTargetId() == Long.parseLong(targetId)) {
                                String documentation = Documentation.encode(rel.getId() + "");
                                element.getModelElement().setDocumentation(documentation);
                                counter++;
                            }
                        }
                    }
                }
            }
        }
        return counter;
    }

    /**
     * returns a list of all outgoing relationships from the element
     * @param decisionElement element to search for outgoing relationships
     * @return list of controlflow elements 
     */
    private List<IControlFlow> findRelationshipsWithStartPointAt(IDiagramElement decisionElement) {
        List<IControlFlow> rtn = new ArrayList<IControlFlow>();
        for (IDiagramElement element :
                currentDiagram.toDiagramElementArray(IModelElementFactory.MODEL_TYPE_CONTROL_FLOW)) {
            IControlFlow flow = (IControlFlow) element.getModelElement();
            if (flow.getFrom().getId().equals(decisionElement.getModelElement().getId())) {
                rtn.add(flow); // :-)
            }
        }
        return rtn;
    }

    /**
     * returns an odr id from a modelelement
     * @param to element which contains the id
     * @return odr id
     */
    private Long getIdByModelElement(IModelElement to) {
        String decisionId = to.getDocumentation();
        return Long.parseLong(Documentation.decode(decisionId));
    }

    /**
     * returns an odr id from a diagramelement
     * @param decisionElement element which contains the id
     * @return odr id
     */
    private Long getIdByDiagramElement(IDiagramElement decisionElement) {
        return getIdByModelElement(decisionElement.getModelElement());
    }

    /**
     * wraps all decisionDTO into decisionDTOWrapper to support
     * óbjects with modified bit
     * @return List<decisionDTOWrapper>
     */
    private List<DecisionDTOWrapper> getDecisionWrapper(ProjectDTO projectDTO) {
        List<DecisionDTOWrapper> rtn = new ArrayList<DecisionDTOWrapper>();
        for (DecisionDTO decision : projectDTO.getDecisions()) {
            DecisionDTOWrapper wrapper = new DecisionDTOWrapper();
            wrapper.setDescription(decision.getDescription());
            wrapper.setHistory(decision.getHistory());
            wrapper.setId(decision.getId());
            wrapper.setName(decision.getName());
            wrapper.setRelationships(decision.getRelationships());
            wrapper.setModified(false);
            rtn.add(wrapper);
        }
        return rtn;
    }

    private void infoAboutDTO(EditDecisionDTO dto) {
        viewManager.showMessage("EDITDECISION INFO");
        viewManager.showMessage("Name: " + dto.getName());
        viewManager.showMessage("ID: " + dto.getId());
        viewManager.showMessage("State: " + dto.getState());
        viewManager.showMessage("Decided when: " + dto.getDecidedWhen());
        viewManager.showMessage("Documented when: " + dto.getDocumentedWhen());
        viewManager.showMessage("Relationship count: " + dto.getRelationshipDTOs().size());
        for (RelationshipDTO rel : dto.getRelationshipDTOs()) {
            viewManager.showMessage("rel id: " + rel.getId());
            viewManager.showMessage("rel type: " + rel.getRelationshipType());
            viewManager.showMessage("rel target id: " + rel.getTargetId());
            for (DecisionDTO dec : projectDTO.getDecisions()) {
                if (dec.getId() == rel.getTargetId()) {
                    viewManager.showMessage("rel target: " + dec.getName());
                }
            }

        }
        viewManager.showMessage("EDITDECISION INFO end");
    }
}