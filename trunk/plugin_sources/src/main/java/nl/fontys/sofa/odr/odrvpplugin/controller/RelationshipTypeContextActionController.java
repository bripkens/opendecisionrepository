/*
 * IMPORTANT !!!!!!!!!!!!!!!!
 * DO NOT DELETE
 * 
 * although this class is 0 times referenced 
 * it's still necessary for plug-in functionality
 * this action controller is set in the plugin.xml
 * 
 * <action id="odr.relationshipTypeContextAction">
 * <actionController class="nl.fontys.sofa.odr.odrvpplugin.controller
 *                          .RelationshipTypeContextActionController"/>
 * </action>
 */
package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPContext;
import com.vp.plugin.action.VPContextActionController;
import com.vp.plugin.model.IControlFlow;
import java.awt.event.ActionEvent;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.StereoTypeMapper;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.dialog.StereotypeSelectionDialog;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;

/**
 *
 * @author Theo Rutten
 */
public class RelationshipTypeContextActionController implements VPContextActionController {

    @Override
    public void performAction(VPAction action, VPContext vpc, ActionEvent ae) {
        ViewManager vm = ApplicationManager.instance().getViewManager();
        ValueRepository repository = ValueRepository.getInstance();

        IControlFlow myControlFlow = (IControlFlow) vpc.getModelElement();

        String[] currentTypes = myControlFlow.toStereotypesArray();
        // TODO: Perhabs re-think the idea of the stereotype window. 
        // When the selected item is a new element (has no active stereotype) 
        //  create a dummy value, else our window doesn't load.
        if (currentTypes.length == 0) {
            currentTypes = new String[]{" "};
        }

        UserSettings userSettings = (UserSettings) repository.getValue(VPStrings.USERSETTINGS);
        // get relationship types from value repo
        List<RelationshipTypeDTO> relationTypes =
                (List<RelationshipTypeDTO>) userSettings.getRelationshipTypes();


        String[] possibleTypes = new String[relationTypes.size()];
        for (int i = 0; i < possibleTypes.length; i++) {
            possibleTypes[i] = relationTypes.get(i).getName();
        }

        StereotypeSelectionDialog dialog =
                new StereotypeSelectionDialog("Select relationship type!", possibleTypes, currentTypes[0]);
        vm.showDialog(dialog);
        if (!dialog.wasCanceled()) {
            String selectedItem = dialog.getSelectedItem();
            StereoTypeMapper.setRelationshipType(vpc, selectedItem);
        }
    }

    @Override
    public void update(VPAction action, VPContext vpc) {
    }
}