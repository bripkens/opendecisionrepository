package nl.rug.search.odr;


import nl.rug.search.odr.entities.Person;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UserTest extends AbstractEjbTest {

    private UserLocal local;

    @Before
    public void setUp() {
        local = lookUp(UserBean.class, UserLocal.class);
    }

    private Person getDummyPerson() {
        Person p = new Person();
        p.setEmail("peron@person.de");
        p.setName("some name");
        p.setPlainPassword("apassword");

        return p;
    }

    @Test
    public void testIsIdSet() {
        Person p = getDummyPerson();

        local.registerPerson(p);
        assertNotNull(p.getPersonId());
    }

    @Test
    public void testIsUsedEmail() {
        Person p = getDummyPerson();

        assertFalse(local.isUsed(p.getEmail()));

        local.registerPerson(p);

        assertTrue(local.isUsed(p.getEmail()));
    }

    @Test
    public void testIsRegistered() {
        Person p = getDummyPerson();

        assertFalse(local.isRegistered(p.getName()));

        local.registerPerson(p);

        assertTrue(local.isRegistered(p.getName()));
    }

    @Test(expected=BusinessException.class)
    public void testRegisterTwice() {
        Person p = getDummyPerson();

        local.registerPerson(p);
        local.registerPerson(p);
    }
}
