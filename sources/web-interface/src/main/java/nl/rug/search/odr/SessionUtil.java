package nl.rug.search.odr;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class SessionUtil implements SessionAttribute {

    public static HttpSession getSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
    }

    public static Object getSingleValue(String key) {
        HttpSession session = getSession(false);

        if (session == null) {
            return null;
        }

        return session.getAttribute(key);
    }

    public static void resetSession() {
        HttpSession session = getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(USER_NAME);
    }
}
