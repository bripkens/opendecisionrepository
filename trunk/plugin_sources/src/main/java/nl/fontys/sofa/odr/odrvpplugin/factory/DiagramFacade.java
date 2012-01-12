/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.factory;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.diagram.IDefaultPackageDiagramUIModel;
import java.util.Date;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.DiagramProperties;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.Serializer;
import nl.fontys.sofa.odr.odrvpplugin.common.TimeSpan;
import nl.fontys.sofa.odr.odrvpplugin.controller.Views.ChronologicalView;
import nl.fontys.sofa.odr.odrvpplugin.controller.Views.RelationshipView;
import nl.fontys.sofa.odr.odrvpplugin.controller.Views.StakeholderInvolvementView;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.IterationDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Administrator
 */
public final class DiagramFacade implements IDiagramFacade {

    private static DiagramManager diagramManager = ApplicationManager.instance().getDiagramManager();

    private DiagramFacade() {
    }

    
    /**
     * creates a new activity diagram and prints a relationship view
     * @param diagramName name of diagram
     * @param props diagramproperties
     * @param decisions decisions to draw
     */
    public static void drawNewRelationshipView(String diagramName, DiagramProperties props, List<DecisionDTO> decisions) {
        assert props != null
                && props.getDiagramType().equals(IDiagramFacade.RELATIONSHIPVIEW)
                && decisions != null;

        IDefaultPackageDiagramUIModel diagram = createEmptyDiagram(props, diagramName, DiagramManager.DIAGRAM_TYPE_ACTIVITY_DIAGRAM);

        RelationshipView view = new RelationshipView(diagram);
        view.drawDiagram(decisions);

        view.layoutDiagram();
    }

    private static IDefaultPackageDiagramUIModel createEmptyDiagram(DiagramProperties props, String diagramName, String diagramType) {
        String xml = Serializer.encode(props, DiagramProperties.class);
        IDefaultPackageDiagramUIModel diagram = (IDefaultPackageDiagramUIModel) diagramManager.createDiagram(diagramType);
        
        diagram.setName(diagramName);
        
        diagram.setDocumentation(Documentation.encode(xml));

        diagramManager.openDiagram(diagram);
        return diagram;
    }

    /**
     * creates a new activity diagram and prints a chronological view
     * @param diagramName name of the diagram
     * @param props diagramprops
     * @param timeSpan timespan inbetween decisions are drawn
     * @param project projectdto
     */
    public static void drawNewChronologicalView(String diagramName, DiagramProperties props, TimeSpan timeSpan, ProjectDTO project) {
        assert props != null
                && props.getDiagramType().equals(IDiagramFacade.CHRONOLOGICALVIEW)
                && project != null;

        IDefaultPackageDiagramUIModel diagram = createEmptyDiagram(props, diagramName, DiagramManager.DIAGRAM_TYPE_ACTIVITY_DIAGRAM);

        ChronologicalView view = new ChronologicalView(diagram);

        if (timeSpan == null) {
            view.drawDiagram(project.getIterations(), project.getDecisions());
        } else {
            Date from = timeSpan.getFrom();
            Date until = timeSpan.getUntil();
            List<IterationDTO> iterations = ChronologicalView.getIterationsInTimeFrame(from, until, project);
            List<DecisionDTO> decisions = ChronologicalView.getDecisionsInTimeFrame(from, until, project);
            view.drawDiagram(iterations, decisions);
        }
        view.layoutDiagram();
    }

    /**
     * creates a new usecase diagram and prints a stakeholderinvolvement view
     * @param diagramName name of the diagram
     * @param props diagramproperties
     * @param decisions decisions to draw
     */
    public static void drawNewStakeholderView(String diagramName, DiagramProperties props, List<DecisionDTO> decisions) {
        assert props != null
                && props.getDiagramType().equals(IDiagramFacade.STAKEHOLDERINVOLVEMENTVIEW)
                && decisions != null;

        IDefaultPackageDiagramUIModel diagram = createEmptyDiagram(props, diagramName, DiagramManager.DIAGRAM_TYPE_USE_CASE_DIAGRAM);
        StakeholderInvolvementView view = new StakeholderInvolvementView(diagram);
        view.drawDiagram(decisions);

        ViewManager viewManager = ApplicationManager.instance().getViewManager();
        viewManager.showMessageDialog(viewManager.getRootFrame(), "will be done in one of the next sprints ;-)");

        view.layoutDiagram();
    }

    /**
     * updates an relationship view by adding new decisions
     * @param decisions decisions to add additionally
     * @param diagram diagram to add to
     */
    public static void updateRelationshipView(List<DecisionDTO> decisions, IDefaultPackageDiagramUIModel diagram) {

        //TODO : Refactor with enums
        String type = Serializer.decode(Documentation.decode(diagram.getDocumentation()), DiagramProperties.class).getDiagramType();
        assert type.equals(DiagramFacade.RELATIONSHIPVIEW);

        new RelationshipView(diagram).updateDiagram(decisions);
    }
}
