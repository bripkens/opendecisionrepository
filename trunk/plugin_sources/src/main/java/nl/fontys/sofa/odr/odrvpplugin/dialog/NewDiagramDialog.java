/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;
import java.awt.Component;

/**
 *
 * @author Tim Aerdts
 */
public class NewDiagramDialog implements IDialogHandler {
    
    private NewDiagramPanel panel;
    private String diagramType;

    public NewDiagramDialog(String diagramType) {
        this.diagramType = diagramType;
    }

    public boolean wasCanceled() {
        return panel.wasCanceled();
    }

    public boolean wasFinished() {
        return panel.wasFinished();
    }

    public boolean wasNext() {
        return panel.wasNext();
    }

    public String getDiagramName() {
        return panel.getDiagramName();
    }

    @Override
    public Component getComponent() {
        panel = new NewDiagramPanel(diagramType);
        return panel;
    }

    @Override
    public void prepare(IDialog id) {
        id.setTitle("New " + diagramType);
        panel.setDialog(id);
        id.setSize(470, 160);
        id.setResizable(false);
    }

    @Override
    public void shown() {
    }

    @Override
    public boolean canClosed() {
        return true;
    }
}
