
package nl.rug.search.odr;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class SessionUtil {
    public static HttpSession getSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
    }
}
