
package nl.rug.search.odr.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.SessionAttribute;
import nl.rug.search.odr.SessionUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class ErrorController {
    private String headline, content;

    @PostConstruct
    public void postConstruct() {
        headline = getMessage(SessionAttribute.ERROR_TITLE, "#{error['unknown.heading']}");
        content = getMessage(SessionAttribute.ERROR_CONTENT, "#{error['unknown.message']}");

        SessionUtil.resetErrorValues();
    }

    private String getMessage(String sessionAttribute, String defaultMessageExpression) {
        Object message = SessionUtil.getSingleValue(sessionAttribute);

        if (message != null) {
            return message.toString();
        }

        return JsfUtil.evaluateExpressionGet(defaultMessageExpression, String.class);
    }

    public String getContent() {
        return content;
    }

    public String getHeadline() {
        return headline;
    }
}
