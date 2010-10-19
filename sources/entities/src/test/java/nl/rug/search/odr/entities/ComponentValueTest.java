
package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ComponentValueTest {
    private ComponentValue c;

    @Before
    public void setUp() {
        c = new ComponentValue();
    }

    @Test
    public void testInitialization() {
        assertNull(c.getId());
        assertNull(c.getValue());
        assertNull(c.getComponent());
    }

    @Test
    public void testId() {
        c.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) c.getId());
    }

    @Test(expected=BusinessException.class)
    public void testIdNull() {
        c.setId(null);
    }

    @Test
    public void testSetValue() {
        c.setValue("foobar");

        assertEquals("foobar", c.getValue());
        
        c.setValue(null);

        assertNull(c.getValue());
    }

    @Test
    public void testSetComponent() {
        TemplateComponent comp = new TemplateComponent();

        c.setComponent(comp);

        assertSame(comp, c.getComponent());
    }

    @Test(expected=BusinessException.class)
    public void testSetNullComponent() {
        c.setComponent(null);
    }

    @Test
    public void testisPersistable() {
        assertFalse(c.isPersistable());

        c.setComponent(new TemplateComponent());

        assertTrue(c.isPersistable());
    }

    @Test
    public void testGetCompareData() {
        assertNull(c.getCompareData()[0]);

        c.setValue("12345");

        assertEquals("12345", c.getCompareData()[0]);
    }
}
