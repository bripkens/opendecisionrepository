
package nl.rug.search.odr.viewpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RequiredFor {
    Viewpoint[] value();
}
