package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class PersonTest {

    private Person p;


    private static <T> boolean containsReference(Collection<T> collection, T item) {
        for(T currentItem : collection) {
            if (currentItem == item) {
                return true;
            }
        }

        return false;
    }


    @Before
    public void setUp() {
        p = new Person();
    }


    @Test
    public void testInitialization() {
        assertNull(p.getPersonId());
        assertNull(p.getName());
        assertNull(p.getPassword());
        assertNull(p.getEmail());

        assertNotNull(p.getMemberships());

        assertTrue(p.getMemberships().isEmpty());
    }



    @Test
    public void testId() {
        p.setPersonId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) p.getPersonId());
    }



    @Test
    public void testSetName() {
        String name = "Hans Gerhard";

        p.setName(name);

        assertEquals(name, p.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidNameNull() {
        p.setName(null);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidNameEmpty() {
        p.setName("  ");
    }






    @Test
    public void testSetPassword() {
        String password = "foo";

        p.setPassword(password);

        assertEquals(password, p.getPassword());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidPasswordNull() {
        p.setPassword(null);
    }

    @Test(expected = RuntimeException.class)
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

    @Test(expected = NullPointerException.class)
    public void testInvalidPlainPasswordNull() {
        p.setPlainPassword(null);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidPlainPasswordEmpty() {
        p.setPlainPassword("  ");
    }







    @Test
    public void testSetEmail() {
        p.setEmail("foo@foo.de");
    }

    @Test(expected = RuntimeException.class)
    public void testSetInvalidEmail1() {
        p.setEmail("dassa");
    }

    @Test(expected = RuntimeException.class)
    public void testSetInvalidEmail2() {
        p.setEmail("dassa@dasdsa");
    }

    @Test(expected = RuntimeException.class)
    public void testSetInvalidEmail3() {
        p.setEmail("dassadasdsa.de");
    }





    @Test
    public void testSetMemberships() {
        Collection<ProjectMember> memberships = new ArrayList<ProjectMember>();

        p.setMemberships(memberships);

        assertNotSame(memberships, p.getMemberships());
    }

    @Test(expected=NullPointerException.class)
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
}
