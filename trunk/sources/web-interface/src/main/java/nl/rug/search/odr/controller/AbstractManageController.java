package nl.rug.search.odr.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import nl.rug.search.odr.RequestParameterAnalyser;
import nl.rug.search.odr.Mode;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class AbstractManageController extends AbstractController implements RequestParameter {

    @EJB
    private ProjectLocal pl;

    // <editor-fold defaultstate="collapsed" desc="abstract methods">
    protected abstract Long getPreviousEntityId();

    protected abstract boolean isPreviousEntitySet();

    protected abstract void resetRequestDependent();

    protected abstract boolean handleCreateExecution();

    protected abstract void handleCreateRequest();

    protected abstract boolean handleUpdateExecution();

    protected abstract boolean handleUpdateRequest(long id);

    protected abstract boolean handleConfirmedDeleteExecution();

    protected abstract boolean handleDeleteRequest(long id);

    // </editor-fold>
    public final boolean isValidRequest() {
        HttpServletRequest request;
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        RequestParameterAnalyser rpa = new RequestParameterAnalyser(request, isPreviousEntitySet());

        if (!isAllowedHook(rpa)) {
            return false;
        }

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
                return handleConfirmedDeleteExecution();
            default:
                return false;
        }
    }

    public final boolean isDeleteRequest() {
        HttpServletRequest request;
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        RequestParameterAnalyser rpa = new RequestParameterAnalyser(request, isPreviousEntitySet());

        if (!isAllowedHook(rpa)) {
            return false;
        }

        Mode mode = rpa.getMode();

        return mode == Mode.DELETE || mode == Mode.DELETE_CONFIRMED;
    }

    protected boolean isAllowedHook(RequestParameterAnalyser rpa) {
        Mode mode = rpa.getMode();
        if (mode == Mode.STAY_IN_CURRENT && !isIdSet()) {
            return true;
        } else if (mode == Mode.STAY_IN_CURRENT) {
            if (!pl.isMember(AuthenticationUtil.getUserId(), getPreviousEntityId())) {
                return false;
            }
            return true;
        }

        if (mode == Mode.DELETE || mode == Mode.DELETE_CONFIRMED) {
            if (!pl.isMember(AuthenticationUtil.getUserId(), rpa.getId())) {
                return false;
            }
            return true;
        }

        if (mode == Mode.CREATE) {
            return true;
        }

        if (mode == Mode.UPDATE) {
            if (!pl.isMember(AuthenticationUtil.getUserId(), rpa.getId())) {
                return false;
            }
            return true;
        }

        return false;
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

    private boolean isIdSet() {
        return getPreviousEntityId() != null;
    }
}
