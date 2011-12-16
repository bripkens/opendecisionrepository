
package nl.rug.search.odr.viewpoint;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ViewpointExclusionStrategy implements ExclusionStrategy {

    private final Viewpoint requiredViewpoint;


    public ViewpointExclusionStrategy(Viewpoint requiredViewpoint) {
        this.requiredViewpoint = requiredViewpoint;
    }




    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        RequiredFor annotation = fa.getAnnotation(RequiredFor.class);

        if (annotation == null) {
            return true;
        }

        for (Viewpoint annotatedViewpoint : annotation.value()) {
            if (annotatedViewpoint == requiredViewpoint) {
                return false;
            }
        }

        return true;
    }




    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }

}
