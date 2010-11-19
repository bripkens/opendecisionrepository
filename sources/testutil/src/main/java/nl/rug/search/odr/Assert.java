package nl.rug.search.odr;

import java.util.Collection;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

public class Assert {

    public static <T> boolean containsReference(Collection<T> collection, T item) {
        for (T currentItem : collection) {
            if (currentItem == item) {
                return true;
            }
        }
        return false;
    }

    public static void assertNotEquals(Object a, Object b){
        assertFalse(a.equals(b));

    }
}
