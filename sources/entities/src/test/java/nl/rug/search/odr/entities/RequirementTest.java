package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 * @modified Ben
 */
public class RequirementTest {

    private Requirement r;




    @Before
    public void setUp() {
        r = new Requirement();
    }




    @Test
    public void testInitialization() {
        assertNull(r.getId());
        assertNull(r.getDescription());
        assertTrue(r.getInitiators().isEmpty());
    }




    @Test
    public void testId() {
        long id = 1;
        r.setId(id);
        assertEquals(1, id);
    }


    @Test(expected=BusinessException.class)
    public void testIdNull() {
        r.setId(null);
    }


    @Test
    public void testSetDescription() {
        String description = "xxx";
        r.setDescription(description);

        assertEquals(description, r.getDescription());
    }




    @Test(expected = BusinessException.class)
    public void testNullDescription() {
        r.setDescription(null);
    }




    @Test(expected = RuntimeException.class)
    public void testEmptyDescription() {
        r.setDescription("     ");
    }



    @Test
    public void testCompareData() {
        String description = "djsiaodasda";
        r.setDescription(description);

        assertEquals(description, r.getCompareData()[0]);
    }


    @Test
    public void testIsPersistable() {
        assertFalse(r.isPersistable());

        r.setDescription("1sdasdsa");

        assertFalse(r.isPersistable());

        r.addInitiator(new ProjectMember());

        assertTrue(r.isPersistable());
    }


    @Test
    public void setProjectMembers() {
        Collection<ProjectMember> initiators = new ArrayList<ProjectMember>();
        ProjectMember initiator = new ProjectMember();
        initiators.add(initiator);

        r.setInitiators(initiators);

        assertNotSame(initiators, r.getInitiators());
        assertSame(initiator, r.getInitiators().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setProjectMembersNull() {
        r.setInitiators(null);
    }




    @Test
    public void addProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.addInitiator(initiator);

        assertFalse(r.getInitiators().isEmpty());

        assertSame(initiator, r.getInitiators().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addProjectMemberNull() {
        r.addInitiator(null);
    }




    @Test
    public void removeProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.addInitiator(initiator);

        assertFalse(r.getInitiators().isEmpty());

        r.removeInitiator(initiator);

        assertTrue(r.getInitiators().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeProjectMemberNull() {
        r.removeInitiator(null);
    }




    @Test
    public void removeAllProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.addInitiator(initiator);

        r.removeAllInitiators();

        assertTrue(r.getInitiators().isEmpty());
    }
}
