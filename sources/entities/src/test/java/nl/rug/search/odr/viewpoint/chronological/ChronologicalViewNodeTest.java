package nl.rug.search.odr.viewpoint.chronological;

import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Version;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ChronologicalViewNodeTest {

    private ChronologicalViewNode n;




    @Before
    public void setUp() {
        n = new ChronologicalViewNode();
    }




    @Test
    public void testInit() {
        assertNull(n.getId());
        assertNull(n.getVersion());
        assertNull(n.getIteration());
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
    public void testIteration() {
        Iteration i = new Iteration();

        n.setIteration(i);

        assertSame(i, n.getIteration());
    }




    @Test
    public void testIsPersistable() {
        assertFalse(n.isPersistable());

        n.setVersion(new Version());

        assertTrue(n.isPersistable());

        n.setIteration(new Iteration());

        assertFalse(n.isPersistable());

        n.setVersion(null);

        assertTrue(n.isPersistable());
    }




    @Test
    public void testCompareData() {
        int x = 3;
        int y = 5;
        boolean visisble = true;
        boolean endpoint = true;

        n.setX(x);
        n.setY(y);
        n.setVisible(visisble);
        n.setEndPoint(endpoint);

        Object[] data = n.getCompareData();

        assertEquals(x, data[0]);
        assertEquals(y, data[1]);
        assertEquals(visisble, data[2]);
        assertEquals(endpoint, data[3]);
    }




    @Test
    public void testSetEndpoint() {
        boolean endpoint = true;
        
        n.setEndPoint(endpoint);
        
        assertEquals(endpoint, n.isEndPoint());
    }




    @Test
    public void testNameComparator() {
        ChronologicalViewNode n1 = new ChronologicalViewNode();
        Version v1 = new Version();
        Decision d1 = new Decision();
        d1.setName("aaaaa");
        v1.setDecision(d1);
        n1.setVersion(v1);

        ChronologicalViewNode n2 = new ChronologicalViewNode();
        Version v2 = new Version();
        Decision d2 = new Decision();
        d2.setName("bbbbb");
        v2.setDecision(d2);
        n2.setVersion(v2);

        assertTrue(new ChronologicalViewNode.DecisionNameComparator().compare(n1, n2) < 0);
    }




}



