package nl.rug.search.odr.controller;

import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.EffectQueue;
import com.icesoft.faces.context.effects.Fade;
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
    private String email, password;

    public static final int RESULT_DELAY = 3;

    private EffectQueue notificationEffect;

    public LoginController() {
        notificationEffect = new EffectQueue("resultEffect");
        notificationEffect.add(new Appear());
        Effect fade = new Fade();
        fade.setDelay(RESULT_DELAY);
        notificationEffect.add(fade);
        notificationEffect.setFired(true);
    }

    public ActionResult submitForm() {
        Person p = null;

        p = ul.tryLogin(email, password);

        if (p == null) {
            notificationEffect.setFired(false);
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EffectQueue getNotificationEffect() {
        return notificationEffect;
    }

    public void setNotificationEffect(EffectQueue notificationEffect) {
        this.notificationEffect = notificationEffect;
    }
}
