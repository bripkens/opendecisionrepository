package nl.rug.search.odr.project;

import org.junit.Ignore;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.entities.StakeholderRole;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

public class StakeholderRoleTest extends AbstractEjbTest {

    private StakeholderRoleLocal srl;

    @Before
    public void setUp() {
        srl = lookUp(StakeholderRoleBean.class, StakeholderRoleLocal.class);
    }

    @Test(expected=BusinessException.class)
    public void testPersistFail() {
        StakeholderRole role = new StakeholderRole();
        srl.persist(role);
    }

    @Test
    public void testPersist() {
        StakeholderRole role = new StakeholderRole();
        role.setName("Some name");
        
        srl.persist(role);

        assertEquals(role, srl.getById(role.getId()));
    }

    @Test
    public void testPublicRole() {
        assertTrue(srl.getPublicRoles().isEmpty());

        StakeholderRole role = new StakeholderRole();
        role.setName("Some name");
        role.setCommon(true);
        srl.persist(role);

        assertFalse(srl.getPublicRoles().isEmpty());

        assertEquals(role, srl.getPublicRoles().iterator().next());
        assertEquals(role, srl.getSomePublicRole());
    }
}
