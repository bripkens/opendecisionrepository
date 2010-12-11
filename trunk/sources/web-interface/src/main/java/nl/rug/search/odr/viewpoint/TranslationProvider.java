
package nl.rug.search.odr.viewpoint;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TranslationProvider {

    public static final String PROPERTIES_FILE = "nl.rug.search.odr.localization.visualization";

    private final Locale locale;
    private final Map<String, String> translations;

    public TranslationProvider(Locale locale) {
        this.locale = locale;

        translations = new HashMap<String, String>();

        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE, locale);

        for (String key : bundle.keySet()) {
            translations.put(key, bundle.getString(key));
        }
    }


}
