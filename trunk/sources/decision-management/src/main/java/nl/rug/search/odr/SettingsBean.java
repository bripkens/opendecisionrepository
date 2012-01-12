package nl.rug.search.odr;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Singleton
public class SettingsBean implements SettingsLocal {
    private static final String PROPERTIES_FILE = "settings.default.properties";
    private static final Logger LOGGER = Logger
            .getLogger(SettingsBean.class.getName());
    
    private Properties p;

    public SettingsBean() {
        p = new Properties();
        
        try {
            p.load(this.getClass().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Can not load default properties file.",
                    ex);
        }
    }
    
    @Lock(LockType.READ)
    @Override
    public String getString(String key) {
        String value = p.getProperty(key);
        
        if (value == null) {
            LOGGER.log(Level.INFO, "Property value for key '{0}' can't be "
                    + "found.", key);
        }
        
        return value;
    }
}
