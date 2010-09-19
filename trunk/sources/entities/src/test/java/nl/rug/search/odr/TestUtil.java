package nl.rug.search.odr;

import java.util.Collection;
import javax.validation.constraints.AssertTrue;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public class TestUtil {

    public static <T> boolean containsReference(Collection<T> collection, T item) {
        for (T currentItem : collection) {
            if (currentItem == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean toStringHelper(Object obj) {
        return obj.toString().startsWith(obj.getClass().getSimpleName());
    }

    public static void assertNotEquals(Object a, Object b){
        assertFalse(a.equals(b));

    }
}
