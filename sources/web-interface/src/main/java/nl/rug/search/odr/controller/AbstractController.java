package nl.rug.search.odr.controller;

import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.EffectQueue;
import com.icesoft.faces.context.effects.Fade;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractController {

    public static final int RESULT_DELAY = 5;
    private UIForm formComponent;
    private EffectQueue resultEffect;

    public AbstractController() {
        resetForm(null);
    }

    public abstract String getSuccessMessage();
    public abstract String getFailMessage();
    public abstract void reset();
    public abstract boolean execute();

    public final void resetForm(ActionEvent e) {
        resultEffect = new EffectQueue("resultEffect");

        resultEffect.add(new Appear());
        Effect fade = new Fade();
        fade.setDelay(RESULT_DELAY);
        resultEffect.add(fade);

        formComponent = null;
        reset();
    }

    public final ActionResult submitForm() {
        boolean success = false;

        try {
            success = execute();
        } catch (Throwable ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            success = false;
        }


        if (!success) {
//            showMessage(FacesMessage.SEVERITY_ERROR, getFailMessage());
            return ActionResult.FAIL;
        }


        showMessage(FacesMessage.SEVERITY_INFO, getSuccessMessage());
        reset();
        
        return ActionResult.SUCCESS;
    }

    private void showMessage(FacesMessage.Severity severity, String msg) {
        clearMessages();

        FacesMessage fm = new FacesMessage(severity, msg, null);
        FacesContext.getCurrentInstance().addMessage(formComponent.getClientId(FacesContext.getCurrentInstance()), fm);

        resultEffect.setFired(false);
    }

    private void clearMessages() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Iterator<String> clients = context.getClientIdsWithMessages();
            while (clients.hasNext()) {
                String clientId = clients.next();
                Iterator<FacesMessage> messages = context.getMessages(clientId);

                while (messages.hasNext()) {
                    messages.next();
                    messages.remove();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Effect getResultEffect() {
        return resultEffect;
    }

    public UIForm getFormComponent() {
        resultEffect.setFired(false);
        return formComponent;
    }

    public void setFormComponent(UIForm formComponent) {
        this.formComponent = formComponent;
    }
}
