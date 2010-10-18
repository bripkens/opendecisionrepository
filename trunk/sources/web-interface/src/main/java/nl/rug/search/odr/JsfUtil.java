package nl.rug.search.odr;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JsfUtil {

    public static <T> T evaluateExpressionGet(String expression, Class<? extends T> expected) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, expression, expected);
    }

    public static void redirect(String url) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        url = externalContext.encodeResourceURL(externalContext.getRequestContextPath().concat(url));

        context.responseComplete();
        externalContext.redirect(url);

    }

    public static void clearMessages() {
        clearMessages(FacesContext.getCurrentInstance());
    }

    public static void clearMessages(FacesContext context) {
        Iterator<String> clients = context.getClientIdsWithMessages();
        while (clients.hasNext()) {
            String clientId = clients.next();
            clearMessages(context, clientId);
        }
    }

    public static void clearMessages(FacesContext context, String clientId) {
        Iterator<FacesMessage> messages = context.getMessages(clientId);

        while (messages.hasNext()) {
            messages.next();
            messages.remove();
        }
    }

    public static void clearMessages(String clientId) {
        clearMessages(FacesContext.getCurrentInstance(), clientId);
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
}
