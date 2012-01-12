package nl.rug.search.odr.util;

import com.icesoft.faces.context.effects.JavascriptContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public final class JsfUtil {

    private JsfUtil() {
    }

    public static <T> T evaluateExpressionGet(String expression, Class<? extends T> expected) {
        return JsfUtil.evaluateExpressionGet(FacesContext.getCurrentInstance(), expression, expected);
    }

    public static <T> T evaluateExpressionGet(FacesContext context,
            String expression,
            Class<? extends T> expected) {
        return context.getApplication().evaluateExpressionGet(context,
                expression, expected);
    }

    public static void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        String encodedUrl = externalContext.encodeResourceURL(externalContext.getRequestContextPath().concat(url));

        context.responseComplete();
        try {
            externalContext.redirect(encodedUrl);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void refreshPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, viewId);
        root.setViewId(viewId);
        context.setViewRoot(root);
    }

    public static void removeSessionBean(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
    }

    public static Flash flashScope() {
        return (FacesContext.getCurrentInstance().getExternalContext().getFlash());
    }

    public static void addJavascriptCall(String call) {
        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), call);
    }

    public static <T> List<T> makeModifiable(Collection<T> original) {
        return new ArrayList<T>(original);
    }
}
