package nl.rug.search.odr;

import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.util.AuthenticationUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class RestrictionEvaluator {

    public static final String IS_ALLOWED = null;

    public static String isAllowed(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return "/error.html";
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['dont.allow.access']}", String.class))) {

            return "/error.html";
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['logged.in']}", String.class)) ||
                expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['group.member']}", String.class))) {
            if (AuthenticationUtil.isAuthtenticated()) {
                return IS_ALLOWED;
            }
            return "/register.html";
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['logged.out']}", String.class))) {
            if (!AuthenticationUtil.isAuthtenticated()) {
                return IS_ALLOWED;
            }
            return "/error.html";
        }

        return "/error.html";
    }
}
