
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class RestrictionEvaluator {

    public static final String IS_ALLOWED = null;

    public static String isAllowed(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return IS_ALLOWED;
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['logged.in']}", String.class))) {
            if (AuthenticationUtil.isAuthtenticated()) {
                return IS_ALLOWED;
            } else {
                return "/register.html";
            }
        }

        if (expression.equals(JsfUtil.evaluateExpressionGet("#{restriction['logged.out']}", String.class))) {
            if (!AuthenticationUtil.isAuthtenticated()) {
                return IS_ALLOWED;
            } else {
                return "/error.html";
            }
        }

        return "/error.html";
    }
}
