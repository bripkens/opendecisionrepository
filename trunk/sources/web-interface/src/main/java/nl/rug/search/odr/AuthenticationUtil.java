
package nl.rug.search.odr;

import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AuthenticationUtil {

    public static void authenticate(Person p) {
        SessionUtil.addValues(
                new String[] {SessionAttribute.USER_ID, SessionAttribute.USER_NAME},
                new Object[] {p.getId(), p.getName()});

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
