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
public class ProjectTest extends AbstractEjbTest {

    @Before
    public void setUp() {
        deleteRecords("Project");
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

        assertEquals(false, projectBean.isUsed("ODR"));

        Project p = new Project();
        p.setName("ODR");
        p.setDescription("keine");

        projectBean.createProject(p);

        assertEquals(true, projectBean.isUsed("odr"));
        assertEquals(true, projectBean.isUsed("ODR"));
    }
}
