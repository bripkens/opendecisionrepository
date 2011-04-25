package nl.rug.search.odr.controller;

import com.sun.faces.util.MessageFactory;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.validator.ValidatorException;
import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.user.UserLocal;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ViewScoped
@ManagedBean
public class RegisterUserController extends AbstractController {

    @EJB
    private UserLocal userLocal;

    private String name, email, password;

    private String passwordConfirmation;




    @Override
    protected String getSuccessMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['registration.success']}", String.class);
    }




    @Override
    protected String getFailMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['registration.fail']}", String.class);
    }




    @Override
    protected void reset() {
        name = email = password = passwordConfirmation = null;
    }




    @Override
    protected boolean execute() {

        Person p = new Person();
        p.setName(name);
        p.setEmail(email);
        p.setPlainPassword(password);


        userLocal.register(p);

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




    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }




    public void setPasswordConfirmation(String passwordConfirmation) {
        if (!passwordConfirmation.equals(password)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "FEHLER", "FEHLER"));
        }
        this.passwordConfirmation = passwordConfirmation;
    }




    public UserLocal getUserLocal() {
        return userLocal;
    }




    public void setUserLocal(UserLocal userLocal) {
        this.userLocal = userLocal;
    }




    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        NavigationBuilder navi = new NavigationBuilder();
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        return navi.getNavigationBar();
    }




}



