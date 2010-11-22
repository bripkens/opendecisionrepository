
package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.List;
import nl.rug.search.odr.entities.Relationship;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static nl.rug.search.odr.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class HandleTest {

    private Handle h;

    @Before
    public void setUp() {
        h = new Handle();
    }

    @Test
    public void testInit() {
        assertNull(h.getId());
        assertEquals(0, h.getX());
        assertEquals(0, h.getY());
    }

    @Test
    public void testId() {
        h.setId(1l);
        assertEquals(1l, (long) h.getId());
    }

    @Test
    public void testX() {
        h.setX(5);
        assertEquals(5, h.getX());
    }

    @Test
    public void testY() {
        h.setY(3);
        assertEquals(3, h.getY());
    }

    @Test
    public void testIsPersistable() {
        assertTrue(h.isPersistable());
    }

    @Test
    public void testCompareData() {
        int x = 3;
        int y = 5;

        h.setX(x);
        h.setY(y);

        Object[] data = h.getCompareData();

        assertEquals(x, data[0]);
        assertEquals(y, data[1]);
    }
}
