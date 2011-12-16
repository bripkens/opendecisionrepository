package nl.rug.search.odr.listener;

import nl.rug.search.odr.util.JsfUtil;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import nl.rug.search.odr.RestrictionEvaluator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class AuthenticationPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1l;




    @Override
    public void beforePhase(PhaseEvent event) {
//        handle(event.getFacesContext());
    }




    @Override
    public void afterPhase(PhaseEvent event) {
        handle(event.getFacesContext());
    }




    private void handle(FacesContext context) {
        UIViewRoot root = context.getViewRoot();

        if (root == null) {
            return;
        }

        String viewId = root.getViewId();
        String restrictionSetting = JsfUtil.evaluateExpressionGet("#{restriction['" + viewId + "']}", String.class);

        if (restrictionSetting == null || restrictionSetting.isEmpty() || restrictionSetting.equals("???".concat(viewId).
                concat("???"))) {
            return;
        }

        String result = RestrictionEvaluator.isAllowed(restrictionSetting);

        if (result != RestrictionEvaluator.IS_ALLOWED) {
            JsfUtil.redirect(result);
        }
    }




    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;

    }
}
