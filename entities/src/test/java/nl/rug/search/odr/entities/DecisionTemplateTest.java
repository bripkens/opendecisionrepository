
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import nl.rug.search.odr.BusinessException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionTemplateTest {

    private DecisionTemplate t;

    @Before
    public void setUp() {
        t = new DecisionTemplate();
    }

    @Test
    public void testInitialization() {
        assertNull(t.getId());
        assertNull(t.getName());
        assertTrue(t.getComponents().isEmpty());
    }

    @Test
    public void testId() {
        t.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) t.getId());
    }

    @Test(expected=BusinessException.class)
    public void testIdNull() {
        t.setId(null);
    }

    @Test
    public void testComparedata() {
        assertEquals(1, t.getCompareData().length);

        assertNull(t.getCompareData()[0]);

        t.setName("Foo");
        assertEquals("Foo", t.getCompareData()[0]);
    }

    @Test
    public void testSetName() {
        String name = "some template";

        t.setName(name);

        assertEquals(name, t.getName());
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameNull() {
        t.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameEmpty() {
        t.setName("  ");
    }

    @Test(expected=BusinessException.class)
    public void testNameTooShort() {
        t.setName("aa ");
    }

    @Test(expected=BusinessException.class)
    public void testNameTooLong() {
        t.setName("aaaaaaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna ");
    }

    @Test
    public void testIsPersistable() {
        assertFalse(t.isPersistable());

        t.setName("foo");

        assertTrue(t.isPersistable());
    }

    @Test
    public void testSetComponents() {
        Collection<TemplateComponent> newCollection = new ArrayList<TemplateComponent>();

        t.setComponents(newCollection);

        assertNotSame(newCollection, t.getComponents());
    }


    @Test(expected=UnsupportedOperationException.class)
    public void testGetComponentsAndDoSomething() {
        t.getComponents().add(null);
    }

    @Test
    public void testAndGetRemoveAllComponents() {
        TemplateComponent comp = new TemplateComponent();

        t.addComponent(comp);

        assertFalse(t.getComponents().isEmpty());

        assertSame(comp, t.getComponents().iterator().next());

        t.removeComponent(comp);

        assertTrue(t.getComponents().isEmpty());

        t.addComponent(comp);

        assertFalse(t.getComponents().isEmpty());

        t.removeAllComponents();

        assertTrue(t.getComponents().isEmpty());
    }

    @Test(expected=BusinessException.class)
    public void testAddNullComponent() {
        t.addComponent(null);
    }

    @Test(expected=BusinessException.class)
    public void testRemoveNullComponent() {
        t.removeComponent(null);
    }

    @Test(expected=BusinessException.class)
    public void testSetNullComponenst() {
        t.setComponents(null);
    }

    @Test
    public void comparatorTest() {
        t.setName("AAAAA");

        DecisionTemplate t2 = new DecisionTemplate();
        t2.setName("BBBBB");

        assertTrue(new DecisionTemplate.NameComparator().compare(t, t2) < 0);
    }
}
