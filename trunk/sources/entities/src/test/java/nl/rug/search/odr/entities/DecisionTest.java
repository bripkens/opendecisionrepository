package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.TestUtil;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class DecisionTest {

    private Decision d;




    @Before
    public void setUp() {
        d = new Decision();
    }




    @After
    public void tearDown() {
    }




    @Test
    public void testInitialization() {
        assertNull(d.getId());
        assertNull(d.getName());
        assertNull(d.getLink());
        assertNull(d.getTemplate());
    }




    @Test
    public void testSetId() {
        d.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) d.getId());
    }




    @Test(expected = BusinessException.class)
    public void testSetNullId() {
        d.setId(null);
    }




    @Test
    public void testSetName() {
        d.setName("foo");
        assertEquals("foo", d.getName());
    }




    @Test(expected = BusinessException.class)
    public void testNullName() {
        d.setName(null);
    }




    @Test(expected = BusinessException.class)
    public void testEmptyName() {
        d.setName("        ");
    }




    @Test(expected = BusinessException.class)
    public void testNameTooShort() {
        d.setName("ab");
    }




    @Test(expected = BusinessException.class)
    public void testNameTooLong() {
        d.setName("123456789012345678901234567890123456789012345678901");
    }




    @Test
    public void testIsPersistable() {
        assertFalse(d.isPersistable());

        d.setName("abcd");

        assertFalse(d.isPersistable());

        d.setTemplate(new DecisionTemplate());

        assertFalse(d.isPersistable());

        d.addVersion(new Version());

        assertTrue(d.isPersistable());
    }




    @Test
    public void testHashCode() {
        Decision at = new Decision();
        at.setId(Long.MIN_VALUE);
        at.setName("bla");

        Decision at1 = new Decision();
        at1.setId(Long.MIN_VALUE);
        at1.setName("bla");

        assertEquals(at.hashCode(), at1.hashCode());
        TestUtil.assertNotEquals(d.hashCode(), at.hashCode());
    }




    @Test
    public void testEquals() {
        assertFalse(d.equals(new TestUtil()));

        Decision at1 = new Decision();
        at1.setName("bla");

        Decision at2 = new Decision();
        at2.setName("foo");

        assertFalse(at1.equals(at2));

        assertTrue(d.equals(d));
    }




    @Test
    public void testNullEquals() {
        assertFalse(d.equals(null));
    }




    @Test
    public void testSetOprLink() {
        OprLink link = new OprLink();

        d.setLink(link);

        assertSame(link, d.getLink());

        d.setLink(null);

        assertNull(d.getLink());
    }




    @Test
    public void setVersionsTest() {
        Collection<Version> versions = new ArrayList<Version>();
        Version version = new Version();
        versions.add(version);


        d.setVersions(versions);

        assertSame(version, d.getVersions().iterator().next());
        assertNotSame(versions, d.getVersions());
    }




    @Test(expected = BusinessException.class)
    public void setVersionsNull() {
        d.setVersions(null);
    }




    @Test
    public void addVersion() {
        Version v = new Version();
        d.addVersion(v);

        assertSame(v, d.getVersions().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addNullVersion() {
        d.addVersion(null);
    }




    @Test
    public void removeVersion() {
        Version v = new Version();
        d.addVersion(v);

        assertFalse(d.getVersions().isEmpty());

        d.removeVersion(v);

        assertTrue(d.getVersions().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeNullVersion() {
        d.removeVersion(null);
    }




    @Test
    public void removeAllVersions() {
        Version v = new Version();
        d.addVersion(v);

        assertFalse(d.getVersions().isEmpty());

        d.removeAllVersions();

        assertTrue(d.getVersions().isEmpty());
    }




    @Test
    public void setTemplate() {
        DecisionTemplate t = new DecisionTemplate();

        d.setTemplate(t);

        assertSame(t, d.getTemplate());
    }




    @Test(expected = BusinessException.class)
    public void setNullTemplate() {
        d.setTemplate(null);
    }




    @Test
    public void setValues() {
        Collection<ComponentValue> values = new ArrayList<ComponentValue>();
        ComponentValue value = new ComponentValue();
        values.add(value);

        d.setValues(values);

        assertNotSame(values, d.getValues());
        assertSame(value, d.getValues().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setValuesNull() {
        d.setValues(null);
    }




    @Test
    public void addValue() {
        ComponentValue value = new ComponentValue();
        d.addValue(value);

        assertFalse(d.getValues().isEmpty());

        assertSame(value, d.getValues().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addValueNull() {
        d.addValue(null);
    }




    @Test
    public void removeValue() {
        ComponentValue value = new ComponentValue();
        d.addValue(value);

        assertFalse(d.getValues().isEmpty());

        d.removeValue(value);

        assertTrue(d.getValues().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeValueNull() {
        d.removeValue(null);
    }




    @Test
    public void removeAllValues() {
        ComponentValue value = new ComponentValue();
        d.addValue(value);

        d.removeAllValues();

        assertTrue(d.getValues().isEmpty());
    }
}
