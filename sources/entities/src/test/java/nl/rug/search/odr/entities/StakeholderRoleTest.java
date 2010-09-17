package nl.rug.search.odr.entities;

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
        assertNull(s.getStakeholderRoleId());
        assertNull(s.getName());
        assertFalse(s.isCommon());
    }

    @Test
    public void testId() {
        s.setStakeholderRoleId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) s.getStakeholderRoleId());
    }


    @Test
    public void testSetName() {
        String name = "admin";

        s.setName(name);

        assertEquals(name, s.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidNameNull() {
        s.setName(null);
    }

    @Test(expected = RuntimeException.class)
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
}
