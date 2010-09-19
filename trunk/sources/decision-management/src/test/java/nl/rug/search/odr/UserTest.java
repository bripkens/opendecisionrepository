package nl.rug.search.odr;


import nl.rug.search.odr.entities.Project;
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
    }

    @After
    public void tearDown() {
    }

    private ProjectLocal getProjectLocal() {
        return lookUp(User.class, ProjectLocal.class);
    }

    @Test
    public void basics() {
        ProjectLocal projectBean = getProjectLocal();

        assertEquals(false, projectBean.isUsed("ODR"));

        Project p = new Project();
        p.setName("ODR");
        p.setDescription("keine");

        projectBean.createProject(p);

        assertEquals(true, projectBean.isUsed("odr"));
        assertEquals(true, projectBean.isUsed("ODR"));
    }
}