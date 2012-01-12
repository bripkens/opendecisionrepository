package nl.rug.search.odr.ws.security;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SkipAuthentication {
}
