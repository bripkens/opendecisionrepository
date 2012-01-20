package nl.rug.search.odr;

import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.util.AuthenticationUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class RestrictionEvaluator {

    private static final String ERROR_PAGE = "/error.html";
    
    public static final String IS_ALLOWED = null;

    private RestrictionEvaluator() {
    }

    public static String isAllowed(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return ERROR_PAGE;
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['dont.allow.access']}", String.class))) {

            return ERROR_PAGE;
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['logged.in']}", String.class))
                || expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['group.member']}", String.class))) {
            if (AuthenticationUtil.isAuthtenticated()) {
                return IS_ALLOWED;
            }
            return "/register.html";
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['logged.out']}", String.class))) {
            if (!AuthenticationUtil.isAuthtenticated()) {
                return IS_ALLOWED;
            }
            return ERROR_PAGE;
        }

        return ERROR_PAGE;
    }
}
