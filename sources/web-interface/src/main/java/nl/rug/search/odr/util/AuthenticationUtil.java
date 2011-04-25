
package nl.rug.search.odr.util;

import javax.servlet.http.HttpSession;
import nl.rug.search.odr.SessionAttribute;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AuthenticationUtil {

    public static void authenticate(Person p) {
        SessionUtil.addValues(
                new String[] {SessionAttribute.USER_ID, SessionAttribute.USER_NAME, SessionAttribute.USER_EMAIL},
                new Object[] {p.getId(), p.getName(), p.getEmail()});

    }
    
    public static void logout() {
        SessionUtil.resetSession();
    }

    public static long getUserId() {
        return (Long) SessionUtil.getSingleValue(SessionAttribute.USER_ID);
    }

    public static long getUserId(HttpSession session) {
        return (Long) session.getAttribute(SessionAttribute.USER_ID);
    }

    public static String getUserName() {
        return SessionUtil.getSingleValue(SessionAttribute.USER_NAME).toString();
    }
    
    public static String getUserEmail() {
        return SessionUtil.getSingleValue(SessionAttribute.USER_EMAIL).toString();
    }

    public static boolean isAuthtenticated() {
        return SessionUtil.getSingleValue(SessionAttribute.USER_ID) != null;
    }

    public static boolean isAuthenticated(HttpSession session) {
        if (session == null) {
            return false;
        }

        return session.getAttribute(SessionAttribute.USER_ID) != null;
    }
}
