package nl.rug.search.odr.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
    public String getSuccessMessage() {
        return "Registration successful.";
    }

    @Override
    public String getFailMessage() {
        return "Registration failed.";
    }

    @Override
    public void reset() {
        name = email = password = null;
    }

    @Override
    public boolean execute() {
        try {

            Person p = new Person();
            p.setName(name);
            p.setEmail(email);
            p.setPassword(password);


            userLocal.registerPerson(p);
        } catch (Exception e) {
            return false;
        }

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
