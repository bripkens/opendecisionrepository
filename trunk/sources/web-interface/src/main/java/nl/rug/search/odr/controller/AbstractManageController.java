package nl.rug.search.odr.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import nl.rug.search.odr.RequestParameterAnalyzer;
import nl.rug.search.odr.Mode;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.RequestParameter;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractManageController extends AbstractController implements RequestParameter {

    protected abstract boolean isIdSet();
    protected abstract boolean isPreviousEntitySet();

    protected abstract void resetRequestDependent();

    protected abstract boolean handleCreateExecution();
    protected abstract void handleCreateRequest();

    protected abstract boolean handleUpdateExecution();
    protected abstract boolean handleUpdateRequest(long id);

    protected abstract boolean handleConfirmedDeleteExecution(long id);
    protected abstract boolean handleDeleteRequest(long id);

    /**
     *
     * @return true when the request should be handled
     */
    protected boolean requestStartHook() {
        return AuthenticationUtil.isAuthtenticated();
    }

    public final boolean isValidRequest() {

        if (!requestStartHook()) {
            return false;
        }

        HttpServletRequest request;
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        RequestParameterAnalyzer rpa = new RequestParameterAnalyzer(request, isPreviousEntitySet());
        Mode mode = rpa.getMode();


        if (mode == Mode.STAY_IN_CURRENT) {
            return true;
        }

        reset();

        switch (mode) {
            case CREATE:
                handleCreateRequest();
                return true;
            case UPDATE:
                return handleUpdateRequest(rpa.getId());
            case DELETE:
                return handleDeleteRequest(rpa.getId());
            case DELETE_CONFIRMED:
                return handleConfirmedDeleteExecution(rpa.getId());
            default:
                return false;
        }
    }


    @Override
    protected boolean execute() {
        boolean success = false;

        try {
            if (isPreviousEntitySet() && !isIdSet()) {
                success = handleCreateExecution();
            } else if (isPreviousEntitySet() && isIdSet()) {
                success = handleUpdateExecution();
            }
        } catch (Throwable ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (success) {
            resetRequestDependent();
        }

        return success;
    }
}
