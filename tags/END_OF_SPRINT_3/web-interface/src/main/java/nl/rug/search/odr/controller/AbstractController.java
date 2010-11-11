package nl.rug.search.odr.controller;

import nl.rug.search.odr.ActionResult;
import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.EffectQueue;
import com.icesoft.faces.context.effects.Fade;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractController implements Serializable {

    public static final int RESULT_DELAY = 3;
    private EffectQueue resultEffect;
    private String resultMessage;
    private ActionResult resultType;

    private final String beanName;

    public AbstractController() {
        String className = getClass().getSimpleName();

        beanName = className.substring(0, 1).toLowerCase().concat(className.substring(1));

        resetForm();
    }

    
    protected abstract String getSuccessMessage();
    protected abstract String getFailMessage();
    protected abstract void reset();
    protected abstract boolean execute();

    protected String getBeanName() {
        return beanName;
    }

    public boolean showMessage() {
        return true;
    }

    public final void resetForm() {
        resultEffect = new EffectQueue("resultEffect");
        resultEffect.add(new Appear());
        Effect fade = new Fade();
        fade.setDelay(RESULT_DELAY);
        resultEffect.add(fade);
        resultEffect.setFired(true);

        resultMessage = null;
        resultType = ActionResult.FAIL;

        reset();
    }

    public void resetForm(ActionEvent e) {
        resetForm();
    }

    public ActionResult submitForm() {
        boolean success = false;

        try {
            success = execute();
        } catch (Throwable ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            success = false;
        }


        if (!success) {
            showMessage(ActionResult.FAIL, getFailMessage());
            return ActionResult.FAIL;
        }


        showMessage(ActionResult.SUCCESS, getSuccessMessage());
        reset();

        return ActionResult.SUCCESS;
    }

    private void showMessage(ActionResult result, String msg) {
        if (!showMessage()) {
            return;
        }
        
        resultMessage = msg;

        resultType = result;

        resultEffect.setFired(false);
    }

    public Effect getResultEffect() {
        return resultEffect;
    }

    public String getResultStyle() {
        if (resultType == ActionResult.SUCCESS) {
            return "success";
        }

        return "fail";
    }

    public String getResultMessage() {
        if (resultMessage == null) {
        }
        return resultMessage;
    }
}