package nl.rug.search.odr.project;


import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.entities.Project;
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

    @Test(expected=BusinessException.class)
    public void basics() {
        ProjectLocal projectBean = getProjectLocal();

        Project p = new Project();
        projectBean.createProject(p);
    }
}
