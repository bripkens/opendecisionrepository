/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPContext;
import com.vp.plugin.action.VPContextActionController;
import com.vp.plugin.diagram.IDiagramUIModel;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sofa.odr.odrvpplugin.common.DiagramProperties;
import nl.fontys.sofa.odr.odrvpplugin.common.Documentation;
import nl.fontys.sofa.odr.odrvpplugin.common.Serializer;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author stefan
 */
public class OdrLinkContextActionController implements VPContextActionController {

    @Override
    public void performAction(VPAction action, VPContext context, ActionEvent e) {
        UserSettings userSettings =
                (UserSettings) ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS);

        IDiagramUIModel model = ApplicationManager.instance().
                getDiagramManager().getActiveDiagram();
        long projectId = Serializer.decode(Documentation.decode(model.getDocumentation()),
                DiagramProperties.class).getProjectId();

        String decisionId = Documentation.decode(context.getModelElement().getDocumentation());
        if (decisionId.isEmpty()) {
            return;
        }

        ProjectDTO prj = (ProjectDTO) ValueRepository.getInstance().getValue(VPStrings.PROJECT);
        DecisionDTO decision = null;
        try {
            for (DecisionDTO dec : prj.getDecisions()) {
                if (dec.getId() == Long.parseLong(decisionId)) {
                    decision = dec;
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            ViewManager vm = ApplicationManager.instance().getViewManager();
            vm.showMessageDialog(vm.getRootFrame(),
                    "Please synchronize the new decision first, before viewing"
                    + " decision details.");
        }
        if (decision == null) {
            return;
        }

        StringBuilder url = new StringBuilder();
        url.append(userSettings.getUrl());
        url.append("/decisionDetails.html?id=");
        url.append(projectId);
        url.append("&versionId=");
        url.append(decision.getHistory().get(0).getId());
        url.append("&decisionId=");
        url.append(decision.getId());

        URI uri = null;
        try {
            uri = new URI(url.toString());
        } catch (URISyntaxException ex) {
            Logger.getLogger(OdrLinkContextActionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void update(VPAction action, VPContext context) {
    }
}
