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

    private final Pattern pattern;

    private final String formatString;

    private final int argCount;

    private Binding parentBinding;

    private final List<Binding> subBindings;

    private Target target;

    Binding(String regex) {
        this(regex, null);
    }

    Binding(String[] args) {
        this(args[0], args[1]);
    }

    Binding(String regex, String formatString) {
        this.pattern = Pattern.compile(regex);
        this.formatString = formatString;

        if (formatString != null) {
            this.argCount = (formatString.length() -
                    formatString.replaceAll("%s", "").length()) / 2;
        } else {
            argCount = 0;
        }

        subBindings = new ArrayList<Binding>();
    }

    Binding bind(String regex) {
        return bind(regex, null);
    }

    Binding bind(String[] args) {
        return bind(args[0], args[1]);
    }

    Binding bind(String regex, String formatString) {
        Binding sub = new Binding(regex, formatString);
        sub.parentBinding = this;
        subBindings.add(sub);
        return sub;
    }

    Binding bind(Binding sub) {
        subBindings.add(sub);
        sub.parentBinding = this;
        return sub;
    }

    Binding to(Target target) {
        this.target = target;
        return this;
    }

    Binding to(String url) {
        this.target = new UrlTarget(url);
        return this;
    }

    public boolean match(String s, HttpServletRequest request,
            HttpServletResponse response) {
        List<String> args = new ArrayList<String>();

        return match(s, args, request, response);
    }

    private boolean match(String s, List<String> args,
            HttpServletRequest request,
            HttpServletResponse response) {
        Matcher m = pattern.matcher(s);
        boolean match = m.lookingAt();

        if (match) {
            s = s.substring(m.end(), s.length());

            for (int i = 0; i < m.groupCount(); i++) {
                // group 0 is always the complete pattern
                args.add(convert(m.group(i + 1)));
            }

            for (Binding subBinding : subBindings) {
                match = subBinding.match(s, args, request, response);

                if (match) {
                    return true;
                }
            }

            if (target != null && s.isEmpty()) {
                target.call(request, response, args);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    protected String convert(String val) {
        return val;
    }

    public String revert(String... args) {
        StringBuilder builder = new StringBuilder();

        revert(args, builder);

        return builder.toString();
    }

    private int revert(String[] args, StringBuilder builder) {
        if (formatString == null) {
            throw new IllegalStateException("This binding has no format "
                    + "string and can therefore not be reverted.");
        }

        int usedArgs = 0;

        if (parentBinding != null) {
            usedArgs += parentBinding.revert(args, builder);
        }

        Object[] argsForFormatting = new Object[argCount];
        for (int i = 0; i < argCount; i++) {
            argsForFormatting[i] = args[usedArgs + i];
        }

        builder.append(String.format(formatString, argsForFormatting));

        return usedArgs += argCount;
    }
}
