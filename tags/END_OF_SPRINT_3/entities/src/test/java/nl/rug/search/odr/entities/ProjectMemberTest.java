package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectMemberTest {

    private ProjectMember m;




    @Before
    public void setUp() {
        m = new ProjectMember();
    }




    @Test
    public void testInit() {
        assertNull(m.getId());
        assertNull(m.getPerson());
        assertNull(m.getProject());
        assertNull(m.getRole());
        assertFalse(m.isRemoved());
    }




    @Test
    public void testAddMembers() {
        Project p = new Project();

        Person person = new Person();
        person.setName("Gerd");

        ProjectMember pm = new ProjectMember();
        pm.setPerson(person);
        pm.setProject(p);

        assertSame(person, p.getMembers().iterator().next().getPerson());
        assertSame(pm, p.getMembers().iterator().next());

        assertSame(p, pm.getProject());
        assertSame(person, pm.getPerson());

        assertSame(p, person.getMemberships().iterator().next().getProject());
        assertSame(pm, person.getMemberships().iterator().next());
    }




    @Test
    public void testRemove() {
        m.setRemoved(true);

        assertTrue(m.isRemoved());

        m.setRemoved(false);

        assertFalse(m.isRemoved());
    }




    @Test
    public void isPersistable() {
        assertFalse(m.isPersistable());

        m.setPerson(new Person());

        assertFalse(m.isPersistable());

        m.setProject(new Project());

        assertFalse(m.isPersistable());

        m.setRole(new StakeholderRole());

        assertTrue(m.isPersistable());
    }




    @Test
    public void getSetRole() {
        StakeholderRole role = new StakeholderRole();

        m.setRole(role);

        assertSame(role, m.getRole());
    }


    @Test(expected = BusinessException.class)
    public void testNullRole() {
        m.setRole(null);
    }



    @Test
    public void testId() {
        m.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) m.getId());
    }




    @Test(expected = BusinessException.class)
    public void testNullId() {
        m.setId(null);
    }


    @Test
    public void testGetCompareData() {
        Person person = new Person();
        Project project = new Project();
        StakeholderRole role = new StakeholderRole();
        boolean removed = true;

        m.setPerson(person);
        m.setProject(project);
        m.setRole(role);
        m.setRemoved(removed);

        assertEquals(person, m.getCompareData()[0]);
        assertEquals(project, m.getCompareData()[1]);
        assertEquals(role, m.getCompareData()[2]);
        assertEquals(removed, m.getCompareData()[3]);
    }



    @Test
    public void testNameComparator() {
        Person pSmall = new Person();
        pSmall.setName("aaa");
        ProjectMember pmSmall = new ProjectMember();
        pmSmall.setPerson(pSmall);

        Person pBig = new Person();
        pBig.setName("bbb");
        ProjectMember pmBig = new ProjectMember();
        pmBig.setPerson(pBig);

        assertTrue(new ProjectMember.NameComparator().compare(pmSmall, pmBig) < 0);

    }
}
