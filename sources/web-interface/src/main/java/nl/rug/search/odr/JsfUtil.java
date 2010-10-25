package nl.rug.search.odr;

import java.io.IOException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JsfUtil {

    public static <T> T evaluateExpressionGet(String expression, Class<? extends T> expected) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, expression, expected);
    }

    public static void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        url = externalContext.encodeResourceURL(externalContext.getRequestContextPath().concat(url));

        context.responseComplete();
        try {
            externalContext.redirect(url);
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

    public static Flash flashScope (){
	return (FacesContext.getCurrentInstance().getExternalContext().getFlash());
   }

}
