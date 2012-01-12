package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPContext;
import com.vp.plugin.action.VPContextActionController;
import com.vp.plugin.model.IActivityAction;
import java.awt.event.ActionEvent;
import java.util.List;
import nl.fontys.sofa.odr.odrvpplugin.common.StereoTypeMapper;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.dialog.StereotypeSelectionDialog;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;

/**
 *
 * @author eigo
 */
public class DecisionStateContextActionController implements
        VPContextActionController {

    @Override
    public void performAction(VPAction action, VPContext context, ActionEvent e) {
        ViewManager vm = ApplicationManager.instance().getViewManager();
        ValueRepository repository = ValueRepository.getInstance();

        String[] currentStates =
                ((IActivityAction) context.getModelElement()).toStereotypeArray();

        /**
         * When the selected item is a new element (has no active stereotype) 
         * create a dummy value, else our window doesn't load.
         */
        if (currentStates.length == 0) {
            currentStates = new String[]{" "};
        }

        // get possible decisions
        UserSettings userSettings =
                (UserSettings) repository.getValue(VPStrings.USERSETTINGS);

        List<DecisionStateDTO> decisions =
                (List<DecisionStateDTO>) userSettings.getDecisionStates();

        String[] possibleStates = new String[decisions.size()];
        for (int i = 0; i < decisions.size(); i++) {
            possibleStates[i] = decisions.get(i).getName();
        }

        StereotypeSelectionDialog dialog =
                new StereotypeSelectionDialog("Select decision sate!",
                possibleStates,
                currentStates[0]);

        vm.showDialog(dialog);
        if (!dialog.wasCanceled()) {
            String selectedItem = dialog.getSelectedItem();
            StereoTypeMapper.setDecisionState(context.getDiagramElement(),
                    selectedItem);
        }
    }

    @Override
    public void update(VPAction action, VPContext context) {
    }
}
