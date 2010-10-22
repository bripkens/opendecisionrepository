package nl.rug.search.odr;

import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Mode;
import nl.rug.search.odr.RequestParameter;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RequestParameterAnalyser implements RequestParameter {

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
