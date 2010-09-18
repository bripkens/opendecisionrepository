package nl.rug.search.odr.controller;

import nl.rug.search.odr.ActionResult;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.UserLocal;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class LoginController {

    @EJB
    private UserLocal ul;
    private String name, password;

    public ActionResult submitForm() {
        Person p = null;

        p = ul.tryLogin(name, password);

        if (p == null) {
            return ActionResult.FAIL;
        }

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        session.setAttribute("logged_in", true);

        JsfUtil.refreshPage();

        return ActionResult.SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
