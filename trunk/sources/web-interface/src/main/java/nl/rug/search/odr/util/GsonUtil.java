package nl.rug.search.odr.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class GsonUtil {

    public static Gson getDefaultGson(ExclusionStrategy strategy) {
        GsonBuilder builder = new GsonBuilder();

        if (strategy != null) {
            builder.setExclusionStrategies(strategy);
        }

        return builder.serializeNulls().
                setDateFormat("yyyy-MM-dd HH:mm z").
                setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).
                create();
    }




    public static Gson getDefaultGson() {
        return getDefaultGson(null);
    }




}



