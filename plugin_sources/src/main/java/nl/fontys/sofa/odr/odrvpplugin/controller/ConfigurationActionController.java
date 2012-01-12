package nl.fontys.sofa.odr.odrvpplugin.controller;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.diagram.IDiagramUIModel;
import com.vp.plugin.diagram.IShapeUIModel;
import com.vp.plugin.model.IActivityAction;
import com.vp.plugin.model.factory.IModelElementFactory;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;
import nl.fontys.sofa.odr.odrvpplugin.dialog.ApplicationConfigurationDialog;
import nl.fontys.sofa.odr.odrvpplugin.dialog.ColorSettingPanel;
import nl.fontys.sofa.odr.odrvpplugin.dialog.UserCredentialsPanel;

/**
 *
 * @author Theo Rutten
 */
public class ConfigurationActionController implements VPActionController {

    @Override
    public void performAction(VPAction action) {
        ViewManager vm = ApplicationManager.instance().getViewManager();

        ApplicationConfigurationDialog dialog =
                new ApplicationConfigurationDialog();
        vm.showDialog(dialog);

        if (!dialog.wasCancelled()) {
            UserCredentialsPanel credentialsPanel =
                    dialog.getUserCredentialsPanel();
            ColorSettingPanel colorSettingsPanel =
                    dialog.getColorSettingsPanel();

            UserSettings userSettings = (UserSettings) ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS);
            Map<Long, Color> changedColorMapping =
                    colorSettingsPanel.getColorMap();
            userSettings.setDecisionStateColorMapping(changedColorMapping);
            userSettings.setUsername(credentialsPanel.getUsernameEntry());
            userSettings.setEncryptedPassword(credentialsPanel.getPasswordEntry());
            userSettings.setUrl(credentialsPanel.getUrlEntry());

            ValueRepository.getInstance().setValue(VPStrings.USERSETTINGS, userSettings);


            File userSettingsFile =
                    new File(VPStrings.USERSETTINGSFILELOCATION);
            
            if (dialog.getColorSettingsPanel().decisionStateColorMappingChanged()) {
                updateDiagramsElementColors();
            }
            try {
                userSettingsFile.createNewFile();

                JAXBContext context =
                        JAXBContext.newInstance(UserSettings.class);
                Marshaller marshaller =
                        context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(userSettings, userSettingsFile);
            } catch (JAXBException jxbEx) {
                //TODO
                vm.showMessage("DEBUG MESSAGE - "
                        + "Failed to marshall user settings: "
                        + jxbEx.getMessage());
            } catch (IOException ex) {
                //TODO
                vm.showMessage("DEBUG MESSAGE - "
                        + "File creation exception: " + ex.getMessage());
            }
        }
    }

    @Override
    public void update(VPAction action) {
    }

    private void updateDiagramsElementColors() {
        IDiagramUIModel[] diagramsInProject =
                ApplicationManager.instance().getProjectManager().getProject().toDiagramArray();

        /**
         *  change all the elements in all the diagrams 
         *  that exist in the current project.
         */
        if (diagramsInProject != null && diagramsInProject.length > 0) {
            for (IDiagramUIModel uiModel : diagramsInProject) {
                updateDiagram(uiModel);
            }
        }
    }

    private void updateDiagram(IDiagramUIModel uiModel) {
        if (uiModel != null && uiModel.getType().equals("ActivityDiagram")) {
            IShapeUIModel[] arr = uiModel.toShapeUIModelArray();
            updateDecisionDiagramElements(arr);
        }
    }

    private void updateDecisionDiagramElements(IShapeUIModel[] elementArray) {
        UserSettings userSettings = (UserSettings) ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS);
        for (IShapeUIModel shape : elementArray) {
            if (shape.getShapeType().equals(IModelElementFactory.MODEL_TYPE_ACTIVITY_ACTION)) {
                String[] decisionStates =
                        ((IActivityAction) shape.getModelElement()).toStereotypeArray();
                Color c = userSettings.getDecisionStateColor(decisionStates[0]);
                shape.getFillColor().setColor1(c);
            }
        }
    }
}
