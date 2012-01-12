package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import nl.fontys.sofa.odr.odrvpplugin.dialog.SelectProjectDialog;

/**
 *
 * @author eigo
 */
public class GetProjectsActionController implements VPActionController {

    @Override
    public void performAction(VPAction action) {
        ViewManager viewManager = 
                ApplicationManager.instance().getViewManager();
        viewManager.showDialog(new SelectProjectDialog());
    }

    @Override
    public void update(VPAction action) {
    }
}
