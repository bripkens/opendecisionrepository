/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;
import java.awt.Component;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;

/**
 *
 * @author Theo Rutten
 */
public class ApplicationConfigurationDialog implements IDialogHandler, VPStrings {

    private ApplicationConfigurationPanel applicationConfigurationPanel;

    @Override
    public Component getComponent() {
        this.applicationConfigurationPanel = new ApplicationConfigurationPanel();
        return applicationConfigurationPanel;
    }

    @Override
    public void prepare(IDialog id) {
        id.setTitle("ODR Configuration Dialog");
        id.setResizable(false);
        id.setSize(850, 650);
        applicationConfigurationPanel.setDialog(id);
    }
    
    public ColorSettingPanel getColorSettingsPanel() {
        return applicationConfigurationPanel.getColorSettingsPanel();
    }
    
    public UserCredentialsPanel getUserCredentialsPanel() {
        return applicationConfigurationPanel.getLoginCredentialsPanel();
    }
    
    @Override
    public void shown() {
    }

    @Override
    public boolean canClosed() {
        return true;
    }

    public boolean wasCancelled() {
        return applicationConfigurationPanel.wasCancelled();
    }
}
