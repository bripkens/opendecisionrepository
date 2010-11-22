package nl.rug.search.odr.viewpoint;

import nl.rug.search.odr.entities.Version;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class NodeTest {

    private Node n;




    @Before
    public void setUp() {
        n = new Node();
    }




    @Test
    public void testInit() {
        assertNull(n.getId());
        assertNull(n.getVersion());
        assertEquals(0, n.getX());
        assertEquals(0, n.getY());
        assertFalse(n.isVisible());
    }




    @Test
    public void testX() {
        int value = 5;
        n.setX(value);

        assertEquals(value, n.getX());
    }




    @Test
    public void testY() {
        int value = 5;
        n.setY(value);

        assertEquals(value, n.getY());
    }




    @Test
    public void testId() {
        long id = 1L;
        n.setId(id);

        assertEquals(id, (long) n.getId());
    }




    @Test
    public void testVisible() {
        n.setVisible(true);

        assertTrue(n.isVisible());
    }




    @Test
    public void testVersion() {
        Version v = new Version();

        n.setVersion(v);

        assertSame(v, n.getVersion());
    }




    @Test
    public void testIsPersistable() {
        assertFalse(n.isPersistable());

        n.setVersion(new Version());

        assertTrue(n.isPersistable());
    }




    @Test
    public void testCompareData() {
        int x = 3;
        int y = 5;
        boolean visisble = true;

        n.setX(x);
        n.setY(y);
        n.setVisible(visisble);

        Object[] data = n.getCompareData();

        assertEquals(x, data[0]);
        assertEquals(y, data[1]);
        assertEquals(visisble, data[2]);
    }
}
