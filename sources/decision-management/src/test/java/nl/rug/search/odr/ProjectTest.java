package nl.rug.search.odr;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectTest extends AbstractEjbTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private ProjectLocal getProjectLocal() {
        return lookUp(ProjectBean.class, ProjectLocal.class);
    }

    @Test
    public void basics() {
        ProjectLocal projectBean = getProjectLocal();

        assertEquals(false, projectBean.isUsed("foo"));
    }
}
