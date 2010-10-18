
package nl.rug.search.odr.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
        headline = SessionUtil.getSingleValue(SessionAttribute.ERROR_TITLE).toString();
        content = SessionUtil.getSingleValue(SessionAttribute.ERROR_CONTENT).toString();

        SessionUtil.resetErrorValues();
    }

    public String getContent() {
        return content;
    }

    public String getHeadline() {
        return headline;
    }
}
