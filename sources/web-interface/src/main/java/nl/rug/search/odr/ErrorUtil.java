
package nl.rug.search.odr;

import java.io.IOException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class ErrorUtil {

    public static void showErrorMessageUsingExpression(String headline, String content) {
        headline = JsfUtil.evaluateExpressionGet(headline, String.class);
        content = JsfUtil.evaluateExpressionGet(content, String.class);

        showErrorMessage(headline, content);
    }

    public static void showErrorMessage(String headline, String content) {
        SessionUtil.addValues(
                new String[] {SessionAttribute.ERROR_TITLE, SessionAttribute.ERROR_CONTENT},
                new String[] {headline, content});

        try {
            JsfUtil.redirect("/error.html");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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
}