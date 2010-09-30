
package nl.rug.search.odr;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AuthenticationUtil {

    public static void authenticate(Person p) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        session.setAttribute(SessionAttribute.USER_ID, p.getId());
        session.setAttribute(SessionAttribute.USER_NAME, p.getName());
    }

    public static void logout() {
        SessionUtil.resetSession();
    }

    public static long getUserId() {
        return (Long) SessionUtil.getSingleValue(SessionAttribute.USER_ID);
    }

    public static String getUserName() {
        return SessionUtil.getSingleValue(SessionAttribute.USER_NAME).toString();
    }

    public static boolean isAuthtenticated() {
        return SessionUtil.getSingleValue(SessionAttribute.USER_ID) != null;
    }
}
