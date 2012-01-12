/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;
// assume imported necessary classes

import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;
import java.awt.Component;

/**
 *
 * @author Vadim Emrich
 */
public class SelectProjectDialog implements IDialogHandler {

    private SelectProjectPanel selectProjectPanel;

    @Override
    public Component getComponent() {
        this.selectProjectPanel = new SelectProjectPanel();
        return selectProjectPanel;
    }

    @Override
    public void prepare(IDialog id) {
        id.setResizable(false);
        id.setSize(600, 300);
        selectProjectPanel.setDialog(id);
    }

    @Override
    public void shown() {
    }

    @Override
    public boolean canClosed() {
        return true;
    }
}
