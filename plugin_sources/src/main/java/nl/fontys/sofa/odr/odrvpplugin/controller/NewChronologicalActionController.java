/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import java.util.Date;
import nl.fontys.sofa.odr.odrvpplugin.common.DiagramProperties;
import nl.fontys.sofa.odr.odrvpplugin.common.TimeSpan;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.dialog.NewDiagramDialog;
import nl.fontys.sofa.odr.odrvpplugin.dialog.SelectProjectDialog;
import nl.fontys.sofa.odr.odrvpplugin.dialog.TimeSpanDialog;
import nl.fontys.sofa.odr.odrvpplugin.factory.DiagramFacade;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author eigo
 */
public class NewChronologicalActionController implements VPActionController {

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
                    new NewDiagramDialog("chronological view");
            vm.showDialog(nameDialog);

            DiagramProperties prop = new DiagramProperties();
            prop.setDiagramType(DiagramFacade.CHRONOLOGICALVIEW);
            prop.setProjectId(project.getId());
            prop.setProjectName(project.getName());

            if (nameDialog.wasNext()) {
                TimeSpanDialog itDialog =
                        new TimeSpanDialog(project.getIterations());
                vm.showDialog(itDialog);
                if (!itDialog.wasCanceled()) {
                    String name = nameDialog.getDiagramName();
                    Date startDate = itDialog.getStartDate();
                    Date endDate = itDialog.getEndDate();
                    TimeSpan timeSpan = new TimeSpan(startDate, endDate);

                    DiagramFacade.drawNewChronologicalView(name,
                            prop,
                            timeSpan,
                            project);
                }
            } else if (nameDialog.wasFinished()) {
                String name = nameDialog.getDiagramName();

                DiagramFacade.drawNewChronologicalView(name,
                        prop,
                        null,
                        project);
            }
        }
    }

    @Override
    public void update(VPAction action) {
    }
}
