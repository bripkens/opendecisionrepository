
package nl.rug.search.odr.tags;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RestrictionEvaluator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@FacesComponent(value="restrictComponent")
public class RestrictAccessTag extends UIComponentBase {

    public static final String ATTRIBUTE_NAME = "allow";
    private String allow;

    public static final String FAMILY = "conditional.restrict";

    @Override
    public String getFamily() {
        return FAMILY;
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        String target = RestrictionEvaluator.isAllowed(getAllow());

        if (target != null) {
            try {
                JsfUtil.redirect(target);
            } catch (IOException ex) {
                throw new RuntimeException("Can't redirect to url ".concat(target), ex);
            }
        }
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    private String getAllow() {
        if (allow != null) {
            return allow;
        }

        ValueExpression expression = getValueExpression(ATTRIBUTE_NAME);

        if (expression == null) {
            throw new RuntimeException("No ".concat(ATTRIBUTE_NAME).concat("attribute defined for tag 'restrict'."));
        }

        return JsfUtil.evaluateExpressionGet(expression.getExpressionString(), String.class);
    }
}
