/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.DiagramProperties;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.dialog.NewDiagramDialog;
import nl.fontys.sofa.odr.odrvpplugin.dialog.SelectProjectDialog;
import nl.fontys.sofa.odr.odrvpplugin.dialog.SelectionDialog;
import nl.fontys.sofa.odr.odrvpplugin.factory.DiagramFacade;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Vadim Emrich
 */
public class NewRelationshipActionController implements VPActionController {

    @Override
    public void performAction(VPAction action) {
        ViewManager vm = ApplicationManager.instance().getViewManager();
        ProjectDTO project = (ProjectDTO) ValueRepository.getInstance().getValue(VPStrings.PROJECT);

        if (project == null) {
            SelectProjectDialog dialog = new SelectProjectDialog();
            vm.showDialog(dialog);
            project = (ProjectDTO) ValueRepository.getInstance().getValue(VPStrings.PROJECT);
        }

        if (project != null) {
            NewDiagramDialog nameDialog =
                    new NewDiagramDialog("relationship view");
            vm.showDialog(nameDialog);

            DiagramProperties prop = new DiagramProperties();
            prop.setDiagramType(DiagramFacade.RELATIONSHIPVIEW);
            prop.setProjectId(project.getId());
            prop.setProjectName(project.getName());

            if (nameDialog.wasNext()) {
                SelectionDialog selectionDialog = new SelectionDialog(project);
                vm.showDialog(selectionDialog);
                if (!selectionDialog.wasCanceled()) {
                    String diagramName = nameDialog.getDiagramName();
                    List<DecisionDTO> selectedDecisions =
                            selectionDialog.getSelectedDecisions();
                    DiagramFacade.drawNewRelationshipView(diagramName,
                            prop,
                            selectedDecisions);

                }
            } else if (nameDialog.wasFinished()) {
                String diagramName = nameDialog.getDiagramName();
                List<DecisionDTO> decisions = project.getDecisions();
                DiagramFacade.drawNewRelationshipView(diagramName,
                        prop,
                        decisions);
            }
        }
    }

    @Override
    public void update(VPAction action) {
    }
}
