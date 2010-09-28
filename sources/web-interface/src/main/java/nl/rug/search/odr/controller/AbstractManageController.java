package nl.rug.search.odr.controller;

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
        System.out.println("======= 1 ======");

        if (!requestStartHook()) {
            System.out.println("======= 2 ======");

            return false;
        }

        HttpServletRequest request;
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        RequestParameterAnalyzer rpa = new RequestParameterAnalyzer(request, isPreviousEntitySet());
        Mode mode = rpa.getMode();

        System.out.println("======= 3 ====== " + mode.toString());

        if (mode == Mode.STAY_IN_CURRENT) {
            return true;
        }

        reset();
        
        System.out.println("======= 4 ======");

        switch (mode) {
            case CREATE:
                System.out.println("======= 5 ======");
                handleCreateRequest();
                return true;
            case UPDATE:
                System.out.println("======= 6 ======");
                return handleUpdateRequest(rpa.getId());
            case DELETE:
                System.out.println("======= 7 ======");
                return handleDeleteRequest(rpa.getId());
            case DELETE_CONFIRMED:
                System.out.println("======= 8 ======");
                return handleConfirmedDeleteExecution(rpa.getId());
            default:
                System.out.println("======= 9 ======");
                return false;
        }
    }


    @Override
    protected boolean execute() {
        resetRequestDependent();

        return false;
    }
}
