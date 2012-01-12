/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.diagram.IDefaultPackageDiagramUIModel;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IDiagramUIModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import nl.fontys.sofa.odr.odrvpplugin.common.DiagramProperties;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.Serializer;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.dialog.SelectProjectDialog;
import nl.fontys.sofa.odr.odrvpplugin.dialog.SelectionDialog;
import nl.fontys.sofa.odr.odrvpplugin.factory.DiagramFacade;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author eigo
 */
public class ImportSelectedActionController implements VPActionController, VPStrings {

    @Override
    public void performAction(VPAction action) {
        ViewManager vm = ApplicationManager.instance().getViewManager();
        ProjectDTO project = (ProjectDTO) ValueRepository.getInstance().getValue(PROJECT);
        IDiagramUIModel activeDiagram = ApplicationManager.instance().getDiagramManager().getActiveDiagram();


        if (project == null) {
            SelectProjectDialog dialog = new SelectProjectDialog();
            vm.showDialog(dialog);
            project = (ProjectDTO) ValueRepository.getInstance().getValue(VPStrings.PROJECT);
        }

        if (project != null) {
            DiagramProperties properties = Serializer.decode(Documentation.decode(activeDiagram.getDocumentation()), DiagramProperties.class);
            if (properties.getDiagramType().equals(DiagramFacade.RELATIONSHIPVIEW)) {
                List<DecisionDTO> selectedDecisions = getExistingDecisions(project, activeDiagram);
                SelectionDialog dialog = new SelectionDialog(project, selectedDecisions);
                vm.showDialog(dialog);
                if (!dialog.wasCanceled()) {
                    List<DecisionDTO> decisions = dialog.getSelectedDecisions();
                    DiagramFacade.updateRelationshipView(decisions, (IDefaultPackageDiagramUIModel) activeDiagram);
                }
            } else {
                vm.showMessageDialog(new JLabel(), "This function is not supported by " + properties.getDiagramType(), "Unsupported Function", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void update(VPAction action) {
    }

    private List<DecisionDTO> getExistingDecisions(ProjectDTO project, IDiagramUIModel activeDiagram) {

        List<DecisionDTO> existing = new ArrayList<DecisionDTO>();
        List<DecisionDTO> possible = project.getDecisions();

        if (activeDiagram != null) {
            Iterator<IDiagramElement> diagramElementIterator = activeDiagram.diagramElementIterator();

            while (diagramElementIterator.hasNext()) {
                IDiagramElement element = diagramElementIterator.next();
                String elementid = Documentation.decode(element.getModelElement().getDocumentation());
                for (DecisionDTO d : possible) {
                    String id = Long.toString(d.getId());
                    if (id.equals(elementid) && !existing.contains(d)) {
                        existing.add(d);
                    }
                }
            }
        }
        return existing;
    }
}