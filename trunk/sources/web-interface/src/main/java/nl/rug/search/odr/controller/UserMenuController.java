
package nl.rug.search.odr.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.SessionUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class UserMenuController {

    public static final String LOGIN_FILENAME = "login.xhtml";
    public static final String USER_MENU_FILENAME = "userMenu.xhtml";

    public String getUserMenu() {
        HttpSession session = SessionUtil.getSession(false);

        if (session == null) {
            return LOGIN_FILENAME;
        }

        Boolean loggedIn = (Boolean) session.getAttribute("logged_in");

        if (loggedIn != null) {
            return USER_MENU_FILENAME;
        } else {
            return LOGIN_FILENAME;
        }
    }

    public void logout() {
        HttpSession session = SessionUtil.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute("logged_in");

        JsfUtil.refreshPage();
    }
}
