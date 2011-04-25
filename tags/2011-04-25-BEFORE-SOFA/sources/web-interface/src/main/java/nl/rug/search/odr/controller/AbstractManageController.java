package nl.rug.search.odr.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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


    private static class RequestParameterAnalyser implements RequestParameter {

        public static final long ID_NOT_SET = -1;

        private final HttpServletRequest request;

        private final boolean previousEntityExisting;

        private boolean create, update, delete, confirm;

        private long id;

        private Mode mode;




        public RequestParameterAnalyser(HttpServletRequest request, boolean previousEntityExisting) {
            this.request = request;
            this.previousEntityExisting = previousEntityExisting;

            readParameters();

            this.mode = determineRequestMode();
        }




        private void readParameters() {
            create = request.getParameter(CREATE) != null;
            update = request.getParameter(UPDATE) != null;
            delete = request.getParameter(DELETE) != null;
            confirm = request.getParameter(CONFIRM) != null;

            String idParam = request.getParameter(ID);

            try {
                id = Long.parseLong(idParam);
            } catch (NumberFormatException ex) {
                id = ID_NOT_SET;
            }

            if (id < 0) {
                id = ID_NOT_SET;
            }
        }




        private Mode determineRequestMode() {
            int paramCounter = 0;
            paramCounter += (create ? 1 : 0);
            paramCounter += (update ? 1 : 0);
            paramCounter += (delete ? 1 : 0);

            if (paramCounter > 1) {
                return Mode.ILLEGAL;
            }

            if (isDeleteSet() && isIdSet()) {

                if (isConfirmSet()) {
                    return Mode.DELETE_CONFIRMED;
                }

                return Mode.DELETE;

            } else if (isCreateSet()) {
                return Mode.CREATE;
            } else if (isUpdateSet() && isIdSet()) {
                return Mode.UPDATE;
            } else if (isPreviousEntityExisting()) {
                return Mode.STAY_IN_CURRENT;
            }

            return Mode.ILLEGAL;
        }

        // <editor-fold defaultstate="collapsed" desc="Getter">



        private boolean isPreviousEntityExisting() {
            return previousEntityExisting;
        }




        public boolean isConfirmSet() {
            return confirm;
        }




        public boolean isCreateSet() {
            return create;
        }




        public boolean isDeleteSet() {
            return delete;
        }




        public long getId() {
            return id;
        }




        public boolean isUpdateSet() {
            return update;
        }




        public boolean isIdSet() {
            return id != ID_NOT_SET;
        }




        public Mode getMode() {
            return mode;
        }




        public boolean isValidRequest() {
            return getMode() != null && getMode() != Mode.ILLEGAL;
        }
        // </editor-fold>
    }
}
