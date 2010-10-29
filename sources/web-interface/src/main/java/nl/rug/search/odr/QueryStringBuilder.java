package nl.rug.search.odr;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class QueryStringBuilder implements RequestParameter {

    private static final Object booleanMarker = new Object();

    private Map<String, Object> values;




    public QueryStringBuilder append(String key, Object value) {
        values.put(key, value);
        return this;
    }




    public QueryStringBuilder appendBolean(String key) {
        values.put(key, booleanMarker);
        return this;
    }




    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (Entry<String, Object> entry : values.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(AMPERSAND);
            }

            builder.append(entry.getKey());
            
            if (entry.getValue() != booleanMarker) {
                builder.append(EQUAL_SIGN);
                builder.append(entry.getValue().toString());
            }
        }

        return builder.toString();
    }
}
