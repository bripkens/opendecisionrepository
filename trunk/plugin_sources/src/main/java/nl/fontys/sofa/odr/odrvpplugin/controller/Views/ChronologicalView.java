/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller.Views;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.diagram.IDefaultPackageDiagramUIModel;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.model.IActivityAction;
import com.vp.plugin.model.IObjectNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.Serializer;
import nl.fontys.sofa.odr.odrvpplugin.common.StereoTypeMapper;
import nl.fontys.sofa.odr.odrvpplugin.common.comparator.DecisionHistoryComparator;
import nl.fontys.sofa.odr.odrvpplugin.common.comparator.IterationDTOComparatorByStartDate;
import nl.fontys.sofa.odr.odrvpplugin.controller.Views.Helper.DecisionHistory;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;
import nl.rug.search.odr.ws.dto.IterationDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Administrator
 */
public class ChronologicalView extends BaseView {

    private DiagramManager diagramManager = ApplicationManager.instance().getDiagramManager();
    private List<IDiagramElement> iterationDiagramElements = new ArrayList<IDiagramElement>();
    private Map<Long, List<List<DecisionHistory>>> iterationToDecisionHistories;
    private static ViewManager vm = ApplicationManager.instance().getViewManager();
    private List<DecisionDTO> decisions = null;
    private List<IterationDTO> iterations = null;

    //TODO javadoc
    public ChronologicalView(IDefaultPackageDiagramUIModel diagram) {
        super(diagram);
        iterationToDecisionHistories = new HashMap<Long, List<List<DecisionHistory>>>();
        this.iterations = new ArrayList<IterationDTO>();
        this.decisions = new ArrayList<DecisionDTO>();
    }

    //TODO javadoc
    public void drawDiagram(List<IterationDTO> iterations, List<DecisionDTO> decisions) {
        Collections.sort(iterations, new IterationDTOComparatorByStartDate());

        this.decisions = decisions;
        this.iterations = iterations;

        // draw iterations
        createIterations();

        createSwimLanesForIteration();

        // draw decisions
        for (int i = 0; i < iterationDiagramElements.size(); i++) {
            long iterationID = Long.parseLong(Documentation.decode(iterationDiagramElements.get(i).getModelElement().getDocumentation()));
            List<List<DecisionHistory>> timelines = iterationToDecisionHistories.get(iterationID);

            //vm.showMessage("------------------------------");
            if (timelines == null || timelines.isEmpty()) {
                if ((i + 1) < iterationDiagramElements.size()) {
//                    vm.showMessage("1 from: " + iterationDiagramElements.get(i).getModelElement().getName()
//                            + " to: " + iterationDiagramElements.get(i).getModelElement().getName());
                    createControlFlow(iterationDiagramElements.get(i), iterationDiagramElements.get(i + 1), null);
                }
                continue;
            }

            for (List<DecisionHistory> timeline : timelines) {
                IDiagramElement prev = iterationDiagramElements.get(i);
                for (DecisionHistory decisionHistory : timeline) {

                    IDiagramElement newOne = createActivityAction(decisionHistory);

//                    vm.showMessage("2 from: " + prev.getModelElement().getName()
//                            + " to: " + newOne.getModelElement().getName());

                    createControlFlow(prev, newOne, null);

                    prev = newOne;
                }
                if ((i + 1) < iterationDiagramElements.size()) {
//                    vm.showMessage("3 from: " + prev.getModelElement().getName()
//                            + " to: " + iterationDiagramElements.get(i + 1).getModelElement().getName());
                    createControlFlow(prev, iterationDiagramElements.get(i + 1), null);
                }
            }
        }

    }

    private void createSwimLanesForIteration() {
        for (IterationDTO iterationDTO : iterations) {
            Date iterationStartDate = iterationDTO.getStartDate();
            Date iterationEndDate = iterationDTO.getEndDate();

            List<DecisionHistory> decisionHistoriesInIteration = new ArrayList<DecisionHistory>();

            //TODO
            vm.showMessage("-----------Iteration: " + iterationDTO.getName());
            // get decision histories which belog to an iteration
            for (DecisionDTO decisionDTO : decisions) {
                for (HistoryDTO historyDTO : decisionDTO.getHistory()) {
                    if (historyDTO.getDecidedWhen().compareTo(iterationStartDate) >= 0
                            && historyDTO.getDecidedWhen().compareTo(iterationEndDate) <= 0) {
                        //TODO
                        vm.showMessage("Decision: " + decisionDTO.getName() + ", State: " + historyDTO.getState());
                        decisionHistoriesInIteration.add(new DecisionHistory(decisionDTO, historyDTO));
                    }
                }
            }

            // make timelines
            List<List<DecisionHistory>> iterationSwimLanes = new ArrayList<List<DecisionHistory>>();
            for (DecisionHistory history : decisionHistoriesInIteration) {

                if (!iterationSwimLanes.isEmpty()) {
                    /*
                     * check if a time line with the same stackholders already exists
                     */
                    List<String> stakeHolders = history.getHistoryDTO().getInitiators();
                    boolean existing = false;
                    for (List<DecisionHistory> timeline : iterationSwimLanes) {
                        List<String> stakeHolersInTimeline = timeline.get(0).getHistoryDTO().getInitiators();

                        // add if stakeholders are equal
                        if (stakeHolders.size() == stakeHolersInTimeline.size()) {
                            existing = true;
                            for (String stakeHolder : stakeHolders) {
                                if (!stakeHolersInTimeline.contains(stakeHolder)) {
                                    existing = false;
                                    break;
                                }
                            }
                            if (existing) {
                                timeline.add(history);
                                break;
                            }
                        }
                    }
                    // add new timeline if not existing
                    if (!existing) {
                        List<DecisionHistory> swimLane = new ArrayList<DecisionHistory>();
                        swimLane.add(history);
                        iterationSwimLanes.add(swimLane);
                    }
                } else {
                    // add first timeline with one element
                    List<DecisionHistory> timeline = new ArrayList<DecisionHistory>();
                    timeline.add(history);
                    iterationSwimLanes.add(timeline);
                }
            }

            for (List<DecisionHistory> timeline : iterationSwimLanes) {
                Collections.sort(timeline, new DecisionHistoryComparator());
            }

            // add iteration timelines
            iterationToDecisionHistories.put(iterationDTO.getId(), iterationSwimLanes);
        }
    }

    private void createIterations() {
        //TODO
        vm.showMessage("---------Create Iterations");
        for (IterationDTO iteration : iterations) {
            //TODO
            vm.showMessage("Iteration: " + iteration.getName());
            IObjectNode objectNode = getFactory().createObjectNode();

            objectNode.setName(iteration.getName());
            objectNode.setDocumentation(Documentation.encode("" + iteration.getId()));
            IDiagramElement displayedIteration = diagramManager.createDiagramElement(getDiagram(), objectNode);
            displayedIteration.setModelElement(objectNode);
            iterationDiagramElements.add(displayedIteration);
        }
    }

    private IDiagramElement createActivityAction(DecisionHistory it) {

        IActivityAction action = getFactory().createActivityAction();
        action.setDocumentation(Documentation.encode(Serializer.encode(it.getDecisionDTO(), DecisionDTO.class)));

        action.addStereotype(it.getHistoryDTO().getState());
        action.setName(it.getDecisionDTO().getName());

        IDiagramElement displayedAction = diagramManager.createDiagramElement(getDiagram(), action);
        displayedAction.setModelElement(action);

        StereoTypeMapper.setDecisionState(displayedAction, it.getHistoryDTO().getState());

        return displayedAction;
    }

    //TODO javadoc
    public void updateDiagram(List<DecisionDTO> decisions) {
        //TODO
        ApplicationManager.instance().getViewManager().showMessage("updateDiagram updateDiagram updateDiagram");
    }

    //TODO javadoc
    public static List<IterationDTO> getIterationsInTimeFrame(Date start, Date end, ProjectDTO project) {
        List<IterationDTO> iterations = new ArrayList<IterationDTO>();

        //TODO
        vm.showMessage("---------Filter Iterations");
        // give iterations which start and/or end in specified time frame
        for (IterationDTO idto : project.getIterations()) {
            if ((idto.getStartDate().compareTo(start) >= 0
                    && idto.getStartDate().compareTo(end) <= 0)
                    || (idto.getEndDate().compareTo(start) >= 0
                    && idto.getEndDate().compareTo(end) <= 0)) {
                iterations.add(idto);
                //TODO
                vm.showMessage("1 Iteration: " + idto.getName());
            }
        }



        // if not iterations where found in this time frame, 
        // give the first iteration started before the startdate
        if (iterations.isEmpty()) {
            //TODO
            vm.showMessage("Iteration list is empty...");
            for (IterationDTO idto : project.getIterations()) {
                if (idto.getStartDate().before(start)
                        && idto.getEndDate().after(end)) {
                    iterations.add(idto);
                    //TODO
                    vm.showMessage("2 Iteration: " + idto.getName());
                }
            }
        }

        return iterations;
    }

    //TODO javadoc
    public static List<DecisionDTO> getDecisionsInTimeFrame(Date start, Date end, ProjectDTO project) {

        //TODO: sort out history by start and end date

        List<DecisionDTO> decisions = new ArrayList<DecisionDTO>();
        //TODO
        vm.showMessage("---------Filter Decisions");
        // get decisions which have history in timeframe
        for (DecisionDTO ddto : project.getDecisions()) {
            for (HistoryDTO hdto : ddto.getHistory()) {
                if (hdto.getDecidedWhen().compareTo(start) >= 0
                        && hdto.getDecidedWhen().compareTo(end) <= 0) {
                    // make a deepcopy
                    //TODO
                    vm.showMessage("Decision: " + ddto.getName());
                    String xml = Serializer.encode(ddto, DecisionDTO.class);
                    DecisionDTO decisionCopy = Serializer.decode(xml, DecisionDTO.class);

                    decisions.add(decisionCopy);
                    break;
                }
            }
        }

        // remove histories which are not in the timeframe
        for (DecisionDTO ddto : decisions) {
            Iterator<HistoryDTO> iterator = ddto.getHistory().iterator();
            while (iterator.hasNext()) {
                HistoryDTO hdto = iterator.next();
                if (hdto.getDecidedWhen().before(start) || hdto.getDecidedWhen().after(end)) {
                    iterator.remove();
                }
            }
        }

        return decisions;
    }
}
