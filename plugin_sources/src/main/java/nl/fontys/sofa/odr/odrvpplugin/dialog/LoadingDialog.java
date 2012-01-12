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
public class LoadingDialog implements IDialogHandler{
    
    private IDialog dialog;

    @Override
    public Component getComponent() {
        return new LoadingPanel();
    }

    @Override
    public void prepare(IDialog dialog) {
        this.dialog = dialog;
        dialog.setSize(400, 120);
        dialog.setResizable(false);
    }

    @Override
    public void shown() {
        
    }

    @Override
    public boolean canClosed() {
        return true;
    }

    public void closeDialog(){
        dialog.close();
    }
   
}
