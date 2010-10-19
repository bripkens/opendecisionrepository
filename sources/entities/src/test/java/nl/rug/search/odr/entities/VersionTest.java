package nl.rug.search.odr.entities;

import java.util.Date;
import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 * @modified Ben
 */
public class VersionTest {

    private Version v;




    @Before
    public void setUp() {
        v = new Version();
    }




    @Test
    public void testInitialization() {
        assertNull(v.getDocumentedWhen());
        assertNull(v.getDecidedWhen());
        assertNull(v.getId());
        assertFalse(v.isRemoved());
    }




    @Test
    public void testVersionId() {
        v.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) v.getId());
    }




    @Test(expected = BusinessException.class)
    public void testNullVersionId() {
        v.setId(null);
    }




    @Test
    public void testSetDecidedWhen() {
        Date someDate = new Date();
        v.setDecidedWhen(someDate);

        assertEquals(someDate, v.getDecidedWhen());
    }




    @Test(expected = BusinessException.class)
    public void testSetDecidedWhenNull() {
        v.setDecidedWhen(null);
    }




    @Test
    public void testSetDocumentedWhen() {
        Date someDate = new Date();
        v.setDocumentedWhen(someDate);

        assertEquals(someDate, v.getDocumentedWhen());
    }




    @Test(expected = BusinessException.class)
    public void testSetDocumentedWhenNull() {
        v.setDocumentedWhen(null);
    }




    @Test
    public void testSetRemoved() {
        v.setRemoved(true);

        assertTrue(v.isRemoved());

        v.setRemoved(false);

        assertFalse(v.isRemoved());
    }




    @Test
    public void testGetCompareData() {
        Date documentedWhen = new Date();
        Date decidedWhen = new Date(documentedWhen.getTime() - 2000);

        v.setDecidedWhen(decidedWhen);
        v.setDocumentedWhen(documentedWhen);

        assertEquals(documentedWhen, v.getCompareData()[0]);
        assertEquals(decidedWhen, v.getCompareData()[1]);
        assertFalse((Boolean) v.getCompareData()[2]);
    }




    @Test
    public void testIsPersistable() {
        assertFalse(v.isPersistable());

        v.setDecidedWhen(new Date());

        assertFalse(v.isPersistable());

        v.setDocumentedWhen(new Date());

        assertTrue(v.isPersistable());
    }




    @Test
    public void testIsPersistableOtherway() {
        assertFalse(v.isPersistable());

        v.setDocumentedWhen(new Date());

        assertFalse(v.isPersistable());

        v.setDecidedWhen(new Date());

        assertTrue(v.isPersistable());
    }
}
