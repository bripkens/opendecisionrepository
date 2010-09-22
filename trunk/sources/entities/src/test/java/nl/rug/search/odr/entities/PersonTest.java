package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import static nl.rug.search.odr.TestUtil.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class PersonTest {

    private Person p;


    @Before
    public void setUp() {
        p = new Person();
    }


    @Test
    public void testInitialization() {
        assertNull(p.getId());
        assertNull(p.getName());
        assertNull(p.getPassword());
        assertNull(p.getEmail());

        assertNotNull(p.getMemberships());

        assertTrue(p.getMemberships().isEmpty());
    }



    @Test
    public void testId() {
        p.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) p.getId());
    }



    @Test
    public void testSetName() {
        String name = "Hans Gerhard";

        p.setName(name);

        assertEquals(name, p.getName());
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameNull() {
        p.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameEmpty() {
        p.setName("  ");
    }

    @Test(expected=BusinessException.class)
    public void testNameTooShort() {
        p.setName("aa ");
    }

    @Test(expected=BusinessException.class)
    public void testNameTooLong() {
        p.setName("aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna ");
    }

    @Test(expected = BusinessException.class)
    public void testChangeName() {
        p.setName("peter");
        p.setName("gabi");
    }
    




    @Test
    public void testSetPassword() {
        String password = "foo";

        p.setPassword(password);

        assertEquals(password, p.getPassword());
    }

    @Test(expected = BusinessException.class)
    public void testInvalidPasswordNull() {
        p.setPassword(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidPasswordEmpty() {
        p.setPassword("  ");
    }

    @Test
    public void testSetPlainPassword() {
        String plainPassword = "foo";

        p.setPlainPassword(plainPassword);

        assertFalse(p.getPassword().equals(plainPassword));

        assertTrue(p.validatePassword(plainPassword));
    }

    @Test(expected = BusinessException.class)
    public void testInvalidPlainPasswordNull() {
        p.setPlainPassword(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidPlainPasswordEmpty() {
        p.setPlainPassword("  ");
    }







    @Test
    public void testSetEmail() {
        p.setEmail("foo@foo.de");
    }

    @Test(expected = BusinessException.class)
    public void testSetInvalidEmail1() {
        p.setEmail("dassa");
    }

    @Test(expected = BusinessException.class)
    public void testSetInvalidEmail2() {
        p.setEmail("dassa@dasdsa");
    }

    @Test(expected = BusinessException.class)
    public void testSetInvalidEmail3() {
        p.setEmail("dassadasdsa.de");
    }

    @Test(expected = BusinessException.class)
    public void testSetInvalidEmail4() {
        p.setEmail(null);
    }





    @Test
    public void testSetMemberships() {
        Collection<ProjectMember> memberships = new ArrayList<ProjectMember>();

        p.setMemberships(memberships);

        assertNotSame(memberships, p.getMemberships());
    }

    @Test(expected=BusinessException.class)
    public void testSetMembershipsNull() {
        p.setMemberships(null);
    }

    @Test
    public void testAddMembership() {
        ProjectMember member = new ProjectMember();
        p.addMembership(member);

        assertTrue(containsReference(p.getMemberships(), member));
    }

    @Test
    public void testRemoveMembership() {
        ProjectMember member = new ProjectMember();
        p.addMembership(member);

        p.removeMembership(member);

        assertFalse(containsReference(p.getMemberships(), member));
    }

    @Test
    public void testUselessThings() {
        p.toString();
    }

    @Test
    public void testAddMembers() {
        Project p = new Project();
        Person person = new Person();
        ProjectMember pm = new ProjectMember();

        person.addMembership(pm);
        pm.setProject(p);

        assertSame(person, p.getMembers().iterator().next().getPerson());
        assertSame(pm, p.getMembers().iterator().next());

        assertSame(p, pm.getProject());
        assertSame(person, pm.getPerson());

        assertSame(p, person.getMemberships().iterator().next().getProject());
        assertSame(pm, person.getMemberships().iterator().next());
    }
}
