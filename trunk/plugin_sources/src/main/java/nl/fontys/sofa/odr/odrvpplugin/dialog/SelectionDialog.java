/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;
import java.awt.Component;
import java.util.List;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Vadim Emrich
 */
public class SelectionDialog implements IDialogHandler{

    private SelectionPanel panel;
    private ProjectDTO project;
    private List<DecisionDTO> existingDecisions = null;

    public SelectionDialog(ProjectDTO project){
        this.project = project;
    }
    
    public SelectionDialog(ProjectDTO project, List<DecisionDTO> existingDecisions) {
        this.project = project;
        this.existingDecisions = existingDecisions;
    }
    
    public List<DecisionDTO> getSelectedDecisions(){
            return panel.getSelectedDecisions();
    }
    
    public boolean wasCanceled(){
        return panel.wasCanceled();
    }
    
    @Override
    public Component getComponent() {
        if(existingDecisions == null){
            this.panel = new SelectionPanel(project);
        }else{
            this.panel = new SelectionPanel(project, existingDecisions);
        }
        return panel;
    }

    @Override
    public void prepare(IDialog dialog) {
        panel.setDialog(dialog);
        dialog.setSize(600, 350);
    }

    @Override
    public void shown() {
    }

    @Override
    public boolean canClosed() {
        return true;
    }
    
}
