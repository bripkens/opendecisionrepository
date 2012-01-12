package nl.rug.search.odr.export;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.antlr.stringtemplate.AttributeRenderer;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class SimpleDateRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o) {
        return toString(o, "yyyy-MM-dd");
    }

    @Override
    public String toString(Object o, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format((Date) o);
    }
    
}
