package nl.rug.search.odr.user;

import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
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

        assertFalse(local.isUsedForFullRegistration(p.getEmail()));

        local.register(p);

        assertTrue(local.isUsedForFullRegistration(p.getEmail()));
    }

    @Test
    public void testIsRegistered() {
        Person p = getDummyPerson();

        assertFalse(local.isRegistered(p.getName()));

        local.register(p);

        assertTrue(local.isRegistered(p.getName()));
    }

    @Test(expected = BusinessException.class)
    public void testRegisterTwice() {
        Person p = getDummyPerson();

        local.register(p);
        local.register(p);
    }

    @Test(expected = BusinessException.class)
    public void tryLoginInvalidEmail() {
        local.tryLogin("dasdsadsa", "12345");
    }

    @Test(expected = BusinessException.class)
    public void tryLoginInvalidEmailNull() {
        local.tryLogin(null, "12345");
    }

    @Test(expected = BusinessException.class)
    public void tryLoginInvalidPasswordNull() {
        local.tryLogin("dsada@dsadas.de", null);
    }

    @Test
    public void tryPreRegisterPerson() {
        String email = "pre@registered.de";

        Person p = local.preRegister(email);

        assertNotNull(p.getId());

        assertFalse(local.isUsedForFullRegistration(email));

        Person p2 = new Person();
        p2.setName("Hans Peter");
        p2.setEmail(email);
        p2.setPlainPassword("12345");
        local.register(p2);

        assertEquals(p.getId(), p2.getId());

        assertTrue(local.isUsedForFullRegistration(email));
    }

    @Test(expected=BusinessException.class)
    public void testTryLoginInvalidEmail() {
        local.tryLogin("notanemailaddress", "1231241");
    }

    @Test(expected=BusinessException.class)
    public void testTryLoginNullPassword() {
        local.tryLogin("notanemailaddress@dadsa.de", null);
    }

    @Test
    public void testTryLoginNotRegistered() {
        assertNull(local.tryLogin("not@registered.de", "dsasda"));
    }

    @Test
    public void testTryLoginInvalidPassword() {
        String email = "registerd@registered.de";
        Person p = new Person();
        p.setName("Some name");
        p.setEmail(email);
        p.setPlainPassword("12345");
        local.register(p);
        
        assertNull(local.tryLogin(email, "54321"));
    }

    @Test
    public void tesValidLogin() {
        String email = "registerd@registered.de";
        String password = "12345";
        
        Person p = new Person();
        p.setName("Some name");
        p.setEmail(email);
        p.setPlainPassword(password);
        local.register(p);

        assertEquals(p, local.tryLogin(email, password));
    }

    @Test
    public void tesInValidLoginPreregistered() {
        String email = "registerd@registered.de";
        String password = "12345";

        local.preRegister(email);

        assertNull(local.tryLogin(email, password));
    }
}
