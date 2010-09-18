package nl.rug.search.odr.controller;

import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.MessageUtil;
import nl.rug.search.odr.UserLocal;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@RequestScoped
@ManagedBean
public class RegisterUserController extends AbstractController {

    @EJB
    private UserLocal userLocal;
    private String name, email, password;

    @Override
    protected String getSuccessMessage() {
        return MessageUtil.getMessageResourceString(
                                    "nl.rug.search.odr.localization.form",
                                    "registration.success",
                                    null,
                                    Locale.ENGLISH);
    }

    @Override
    protected String getFailMessage() {
        return MessageUtil.getMessageResourceString(
                                    "nl.rug.search.odr.localization.form",
                                    "registration.fail",
                                    null,
                                    Locale.ENGLISH);
    }

    @Override
    protected void reset() {
        name = email = password = null;
    }

    @Override
    protected boolean execute() {

        Person p = new Person();
        p.setName(name);
        p.setEmail(email);
        p.setPlainPassword(password);


        userLocal.registerPerson(p);

        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserLocal getUserLocal() {
        return userLocal;
    }

    public void setUserLocal(UserLocal userLocal) {
        this.userLocal = userLocal;
    }
}
