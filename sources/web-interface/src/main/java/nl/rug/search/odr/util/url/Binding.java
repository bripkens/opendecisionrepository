
package nl.rug.search.odr.util.url;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Binding {

    private final Pattern p;

    private List<Binding> subBindings;
    private Target target;

    public Binding(String s) {
        p = Pattern.compile(s);

        subBindings = new ArrayList<Binding>();
    }

    public Binding bind(String s) {
        Binding sub = new Binding(s);
        subBindings.add(sub);
        return sub;
    }

    public Binding bind(Binding sub) {
        subBindings.add(sub);
        return sub;
    }

    public Binding to(Target target) {
        this.target = target;
        return this;
    }

    public Binding to(String url) {
        this.target = new UrlTarget(url);
        return this;
    }

    public boolean check(String s, HttpServletRequest request,
            HttpServletResponse response) {
        List<String> args = new ArrayList<String>();

        return check(s, args, request, response);
    }

    private boolean check(String s, List<String> args, HttpServletRequest request,
            HttpServletResponse response) {
        Matcher m = p.matcher(s);
        boolean match = m.lookingAt();

        if (match) {
            s = s.substring(m.end(), s.length());

            for(int i = 0; i < m.groupCount(); i++) {
                // group 0 is always the complete pattern
                args.add(convert(m.group(i+1)));
            }

            for (Binding subBinding : subBindings) {
                match = subBinding.check(s, args, request, response);

                if (match) {
                    return true;
                }
            }

            target.call(request, response, args);
            return true;
        } else {
            return false;
        }
    }

    protected String convert(String val) {
        return val;
    }
}
