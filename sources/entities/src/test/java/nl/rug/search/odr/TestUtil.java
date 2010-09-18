package nl.rug.search.odr;

import java.util.Collection;
import org.junit.Ignore;

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
}
