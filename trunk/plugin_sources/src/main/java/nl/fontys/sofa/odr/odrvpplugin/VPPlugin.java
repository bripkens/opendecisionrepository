package nl.fontys.sofa.odr.odrvpplugin;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.VPPluginInfo;
import com.vp.plugin.ViewManager;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import nl.fontys.sofa.odr.odrvpplugin.common.UserSettings;
import nl.fontys.sofa.odr.odrvpplugin.common.VPStrings;
import nl.fontys.sofa.odr.odrvpplugin.common.ValueRepository;

/**
 * Hello world!
 *
 */
public class VPPlugin implements com.vp.plugin.VPPlugin {

    private ViewManager viewManager = ApplicationManager.instance().getViewManager();

    @Override
    public void loaded(VPPluginInfo pluginInfo) {
        viewManager.showMessage("ODR plugin loaded");

        File userSettingsFile = new File(VPStrings.USERSETTINGSFILELOCATION);

        if (userSettingsFile.exists()) {
            try {
                //de-serialize the usersettings
                JAXBContext context = JAXBContext.newInstance(UserSettings.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                UserSettings userSettings = (UserSettings) unmarshaller.unmarshal(userSettingsFile);
                // store the usersettings in the value repository
                ValueRepository valueRepository = ValueRepository.getInstance();
                valueRepository.setValue(VPStrings.USERSETTINGS, userSettings);
            } catch (JAXBException jxbEx) {
                //de-serialization failed
                createAndStoreDefaultUserSettings();
            }
        } else {
            createAndStoreDefaultUserSettings();
        }
    }

    @Override
    public void unloaded() {
    }

    private void createAndStoreDefaultUserSettings() {
        File userSettingsFile = new File(VPStrings.USERSETTINGSFILELOCATION);

        try {
            String pluginDirectory = ApplicationManager.instance().getPluginInfo("sample.plugin").getPluginDir().toString();
            File defaultFile = new File(pluginDirectory + VPStrings.FILESEPARATOR + VPStrings.USERSETTINGSFILE);

            JAXBContext context = JAXBContext.newInstance(UserSettings.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserSettings userSettings = (UserSettings) unmarshaller.unmarshal(defaultFile);

            File pluginWorkingDirectory = new File(VPStrings.USERSETTINGSFOLDER);
            pluginWorkingDirectory.mkdir();

            userSettingsFile.createNewFile();
            // if the default usersettings has been created, save it in the value repository
            ValueRepository valueRepository = ValueRepository.getInstance();
            valueRepository.setValue(VPStrings.USERSETTINGS, userSettings);

            // serialise it to the file.

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userSettings, userSettingsFile);
        } catch (JAXBException jxbEx) {
            //viewManager.
        } catch (IOException ex) {
        }
    }
}
