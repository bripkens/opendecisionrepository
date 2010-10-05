
package nl.rug.search.odr;

import java.io.IOException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class ErrorUtil {

    public static void showErrorMessage(String headline, String content) {
        SessionUtil.addValues(
                new String[] {SessionAttribute.ERROR_TITLE, SessionAttribute.ERROR_CONTENT},
                new String[] {headline, content});
        
        try {
            JsfUtil.redirect("/error.html");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
