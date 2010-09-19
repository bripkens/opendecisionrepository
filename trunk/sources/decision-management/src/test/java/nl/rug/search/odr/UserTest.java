package nl.rug.search.odr;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UserTest extends AbstractEjbTest {

    @Before
    public void setUp() {
        deleteRecords("Person");
    }

    @After
    public void tearDown() {
    }

    private UserLocal getUserLocal() {
        return lookUp(User.class, UserLocal.class);
    }

    @Test
    public void basics() {
        assertTrue(true);
    }
}
