package nl.rug.search.odr.controller.decision;

import java.util.Comparator;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.TemplateComponent;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TemplateComponentInput {

    private String value;

    private final boolean last;

    private final TemplateComponent tc;

    private final ComponentValue cv;


    public TemplateComponentInput(TemplateComponent tc, ComponentValue cv, boolean last) {
        this.tc = tc;
        this.last = last;

        if (cv != null) {
            this.cv = cv;
            value = cv.getValue();
        } else {
            this.cv = new ComponentValue();
            this.cv.setComponent(tc);
        }
    }




    public TemplateComponentInput(TemplateComponent tc, boolean last) {
        this(tc, null, last);
    }



    public ComponentValue getComponentValue() {
        cv.setValue(value);
        return cv;
    }



    public String getLabel() {
        return tc.getLabel();
    }




    public boolean isLast() {
        return last;
    }




    public int getOrder() {
        return tc.getOrder();
    }




    public String getValue() {
        return value;
    }




    public void setValue(String value) {
        this.value = value;
    }




    public static class OrderComparator implements Comparator<TemplateComponentInput> {

        @Override
        public int compare(TemplateComponentInput o1, TemplateComponentInput o2) {
            return o1.getOrder() - o2.getOrder();
        }

    }
}
