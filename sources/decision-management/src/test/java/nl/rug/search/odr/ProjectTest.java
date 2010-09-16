
package nl.rug.search.odr;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectTest {

    private static EJBContainer ejbC;
    private static Context ctx;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ejbC = EJBContainer.createEJBContainer();

        ctx = ejbC.getContext();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ejbC.close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    @Test
    public void basics() {
        try {
            ProjectLocal projectBean = (ProjectLocal) ctx.lookup("java:global/classes/ProjectBean");

            assertNotNull(projectBean);

            assertEquals(false, projectBean.isUsed("foo"));
        } catch (NamingException ex) {
            Logger.getLogger(ProjectTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}