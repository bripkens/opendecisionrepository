/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;
import java.awt.Component;
import java.util.Date;
import java.util.List;
import nl.rug.search.odr.ws.dto.IterationDTO;

/**
 *
 * @author eigo
 */
public class TimeSpanDialog implements IDialogHandler {

    private TimeSpanPanel panel;
    private List<IterationDTO> iterations;

    public TimeSpanDialog(List<IterationDTO> iterations) {
        this.iterations = iterations;
    }

    @Override
    public Component getComponent() {
        panel = new TimeSpanPanel(iterations);
        return panel;
    }

    @Override
    public void prepare(IDialog dialog) {
        dialog.setSize(470, 320);
        dialog.setResizable(false);
        dialog.setTitle("Select Time Span");
        panel.setDialog(dialog);
    }

    @Override
    public void shown() {
    }

    @Override
    public boolean canClosed() {
        return true;
    }

    public boolean wasCanceled() {
        return panel.wasCanceled();
    }

    public Date getStartDate() {
        return panel.getStartDate();
    }

    public Date getEndDate() {
        return panel.getEndDate();
    }
}
