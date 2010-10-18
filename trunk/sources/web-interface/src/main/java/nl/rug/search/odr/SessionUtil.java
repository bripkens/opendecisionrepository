package nl.rug.search.odr;

import java.util.Map;
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

    public static void addValues(String[] keys, Object[] values) {
        HttpSession session = getSession(true);

        for (int i = 0; i < keys.length; i++) {
            session.setAttribute(keys[i], values[i]);
        }
    }

    public static void removeValues(String... keys) {
        HttpSession session = getSession(false);

        if (session == null) {
            return;
        }

        for (String each : keys) {
            session.removeAttribute(each);
        }
    }

    public static void resetSession(HttpSession session) {
        if (session == null) {
            return;
        }

        session.removeAttribute(USER_ID);
        session.removeAttribute(USER_NAME);
        session.removeAttribute(ERROR_TITLE);
        session.removeAttribute(ERROR_CONTENT);
    }

    public static void resetErrorValues() {
        HttpSession session = getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(ERROR_TITLE);
        session.removeAttribute(ERROR_CONTENT);
    }

    public static void resetSession() {
        resetSession(getSession(false));
    }
}
