
package nl.rug.search.odr.tags;

import java.io.IOException;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import nl.rug.search.odr.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ConditionalRedirectTag extends UIComponentELTag {

    @Override
    public String getComponentType() {
        return "con-redirect";
    }

    @Override
    public String getRendererType() {
        return null;
    }

//    @Override
//    public int doStartTag() throws JspException {
//
//        String when = getWhen();
//
//        if (when == null || JsfUtil.evaluateExpressionGet(when, Boolean.class)) {
//            try {
//                JsfUtil.redirect(getUrl());
//            } catch (IOException ex) {
//                throw new JspException(ex);
//            }
//        }
//
//        return super.doStartTag();
//    }
//
//
//    private String getUrl() {
//        return super.getValue("url").toString();
//    }
//
//    private String getWhen() {
//        return super.getValue("when").toString();
//    }
}
