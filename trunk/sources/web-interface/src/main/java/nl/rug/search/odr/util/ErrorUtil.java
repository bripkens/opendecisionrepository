package nl.rug.search.odr.util;

import nl.rug.search.odr.Filename;
import nl.rug.search.odr.SessionAttribute;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class ErrorUtil {

    private ErrorUtil() {
    }

    public static void showErrorMessageUsingExpression(String headline,
            String content) {
        String evluatedHeadline = JsfUtil.evaluateExpressionGet(headline,
                String.class);
        String evluatedContent = JsfUtil.evaluateExpressionGet(content,
                String.class);

        showErrorMessage(evluatedHeadline, evluatedContent);
    }

    public static void showErrorMessage(String headline, String content) {
        SessionUtil.addValues(
                new String[]{SessionAttribute.ERROR_TITLE, SessionAttribute.ERROR_CONTENT},
                new String[]{headline, content});

        JsfUtil.redirect(Filename.ERROR_WITH_LEADING_SLASH);
    }

    public static void showUknownError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['unknown.heading']}",
                "#{error['unknown.message']}");
    }

    public static void showNotAuthenticatedError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['not.authenticated.heading']}",
                "#{error['not.authenticated.message']}");
    }

    public static void showInvalidIdError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['invalid.id.heading']}", "#{error['invalid.id.message']}");
    }

    public static void showIdNotRegisteredError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['not.registered.id.heading']}",
                "#{error['not.registered.id.message']}");
    }

    public static void showNoMemberError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['not.member.heading']}", "#{error['not.member.message']}");
    }

    public static void showIterationIdNotRegisteredError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['not.registered.id.heading']}",
                "#{error['not.registered.id.message']}");
    }

    public static void showNoPermissionToAccessConcernError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['no.permission.concern.id.heading']}",
                "#{error['no.permission.concern.id.message']}");
    }

    public static void showInvalidParametersError() {
        ErrorUtil.showErrorMessageUsingExpression("#{error['invalid.parameter.heading']}",
                "#{error['invalid.parameter.message']}");
    }
}
