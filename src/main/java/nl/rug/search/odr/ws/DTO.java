package nl.rug.search.odr.ws;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DTO {
}
