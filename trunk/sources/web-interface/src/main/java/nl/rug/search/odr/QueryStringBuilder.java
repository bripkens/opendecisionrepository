package nl.rug.search.odr;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class QueryStringBuilder implements RequestParameter {

    private static final Boolean BOOLEAN_MARKER = true;

    private String url;

    private Map<String, Object> values;




    public QueryStringBuilder() {
        values = new HashMap<String, Object>();
    }




    public QueryStringBuilder setUrl(String name) {
        this.url = name;
        return this;
    }




    public QueryStringBuilder append(String key, Object value) {
        values.put(key, value);
        return this;
    }




    public QueryStringBuilder appendBolean(String key) {
        values.put(key, BOOLEAN_MARKER);
        return this;
    }




    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (url != null) {
            builder.append(url);
            builder.append(QUESTION_MARK);
        }

        boolean first = true;
        for (Entry<String, Object> entry : values.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(AMPERSAND);
            }

            builder.append(entry.getKey());

            builder.append(EQUAL_SIGN);
            builder.append(entry.getValue().toString());
        }

        return builder.toString();
    }




}



