package nl.rug.search.odr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public final class Assert {

    private  Assert() {
    }

    public static <T> boolean containsReference(Collection<T> collection, T item) {
        for (T currentItem : collection) {
            if (currentItem == item) {
                return true;
            }
        }
        return false;
    }

    public static <T> void assertContainsReference(T item, Collection<T> collection) {
        assertTrue(containsReference(collection, item));
    }

    public static <T> void assertContainsNoReference(T item, Collection<T> collection) {
        assertFalse(containsReference(collection, item));
    }

    public static void assertNotEquals(Object a, Object b) {
        assertFalse(a.equals(b));
    }

    public static void genericConstructorTest(Class<?> cls) throws
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        final Constructor<?> c = cls.getDeclaredConstructors()[0];
        c.setAccessible(true);
        final Object n = c.newInstance((Object[]) null);

        assertNotNull(n);
    }
}
