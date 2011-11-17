package nl.rug.search.odr.export;

import org.antlr.stringtemplate.AttributeRenderer;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TeXEscapeRenderer implements AttributeRenderer {

    private static final String ESCAPE_OPENING_BRACE = "-\"-{-\"-",
            ESCAPE_CLOSING_BRACE = "-\"-}-\"-";
    
    @Override
    public String toString(Object o) {
        // TODO escape properly
        return o.toString()
                .replace("{", ESCAPE_OPENING_BRACE)
                .replace("}", ESCAPE_CLOSING_BRACE)
                .replace("\\", "\\textbackslash{}")
                .replace("#", "\\#{}")
                .replace("$", "\\${}")
                .replace("%", "\\%{}")
                .replace("^", "\\textasciicircum{}")
                .replace("&", "\\&{}")
                .replace("_", "\\_{}")
                .replace("~", "\\~{}")
                .replace("<", "\\textless{}")
                .replace(">", "\\textgreater{}")
                .replace(ESCAPE_OPENING_BRACE, "\\{{}")
                .replace(ESCAPE_CLOSING_BRACE, "\\}{}");
    }

    @Override
    public String toString(Object o, String formatName) {
        return toString(o);
    }
    
}
