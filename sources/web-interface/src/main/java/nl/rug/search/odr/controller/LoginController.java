package nl.rug.search.odr.controller;

import java.io.IOException;
import nl.rug.search.odr.ActionResult;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.user.UserLocal;
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

        AuthenticationUtil.authenticate(p);
        try {
            JsfUtil.redirect("/projects.html");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

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
