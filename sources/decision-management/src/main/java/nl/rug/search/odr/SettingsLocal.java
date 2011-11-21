package nl.rug.search.odr;

import javax.ejb.Local;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface SettingsLocal {

    String getString(String key);
    
}
