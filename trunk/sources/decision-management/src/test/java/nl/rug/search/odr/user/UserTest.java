package nl.rug.search.odr.user;


import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.user.UserBean;
import nl.rug.search.odr.user.UserLocal;
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

        local.register(p);
        assertNotNull(p.getId());
    }

    @Test
    public void testIsUsedEmail() {
        Person p = getDummyPerson();

        assertFalse(local.isUsed(p.getEmail()));

        local.register(p);

        assertTrue(local.isUsed(p.getEmail()));
    }

    @Test
    public void testIsRegistered() {
        Person p = getDummyPerson();

        assertFalse(local.isRegistered(p.getName()));

        local.register(p);

        assertTrue(local.isRegistered(p.getName()));
    }

    @Test(expected=BusinessException.class)
    public void testRegisterTwice() {
        Person p = getDummyPerson();

        local.register(p);
        local.register(p);
    }

    @Test(expected=BusinessException.class)
    public void tryLoginInvalidEmail() {
        local.tryLogin("dasdsadsa", "12345");
    }

    @Test(expected=BusinessException.class)
    public void tryLoginInvalidEmailNull() {
        local.tryLogin(null, "12345");
    }

    @Test(expected=BusinessException.class)
    public void tryLoginInvalidPasswordNull() {
        local.tryLogin("dsada@dsadas.de", null);
    }
}
