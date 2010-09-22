package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class StakeholderRoleTest {

    private StakeholderRole s;

    @Before
    public void setUp() {
        s = new StakeholderRole();
    }

    @Test
    public void testInit() {
        assertNull(s.getId());
        assertNull(s.getName());
        assertFalse(s.isCommon());
    }

    @Test
    public void testId() {
        s.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) s.getId());
    }


    @Test
    public void testSetName() {
        String name = "admin";

        s.setName(name);

        assertEquals(name, s.getName());
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameNull() {
        s.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameEmpty() {
        s.setName("  ");
    }



    @Test
    public void testCommon() {
        s.setCommon(true);

        assertTrue(s.isCommon());

        s.setCommon(false);

        assertFalse(s.isCommon());
    }

    @Test
    public void testIsEqual() {
        s.setName("1");
        s.setCommon(true);

        StakeholderRole s2 = new StakeholderRole();
        s2.setName("1");
        s2.setCommon(true);

        assertEquals(s, s2);
    }
}
