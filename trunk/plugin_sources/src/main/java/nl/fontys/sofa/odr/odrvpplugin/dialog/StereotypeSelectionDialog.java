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
 * @author eigo
 */
public class StereotypeSelectionDialog implements IDialogHandler {

    private StereotypeSelectionPanel panel;
    private String title;
    private String[] items;
    private String selectedItem;

    public StereotypeSelectionDialog(String title, String[] items, String selectedItem) {
        this.title = title;
        this.items = items.clone();
        this.selectedItem = selectedItem;
    }

    public String getSelectedItem() {
        return panel.getSelectedItem();
    }

    public boolean wasCanceled() {
        return panel.wasCanceled();
    }

    @Override
    public Component getComponent() {
        panel = new StereotypeSelectionPanel(title, items, selectedItem);
        return panel;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setTitle("Selection");
        dialog.setResizable(false);
        dialog.setSize(350, 140);
        panel.setDialog(dialog);
    }

    @Override
    public void shown() {
    }

    @Override
    public boolean canClosed() {
        return true;
    }
}