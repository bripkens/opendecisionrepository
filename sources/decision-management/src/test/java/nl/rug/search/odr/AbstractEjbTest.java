
package nl.rug.search.odr;

import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public class AbstractEjbTest {

    protected final static EJBContainer ejbC;
    protected final static Context ctx;

    static {
        ejbC = EJBContainer.createEJBContainer();

        ctx = ejbC.getContext();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Moved to class initialization block to reduce test execution time
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // the ejb container should be closed, but to reduce test execution time
        // it has been removed.
//        ejbC.close();
    }

    public static <T> T lookUp(Class classType, Class<T> interfaceType) {
        T lookedUpClass = null;
        
        try {
            lookedUpClass = (T) ctx.lookup("java:global/classes/" + classType.getSimpleName());
        } catch (NamingException ex) {
            fail("Could not lookup " + classType.getName());
        }

        assertNotNull("Looked up class " + classType.getName() + " is null.", lookedUpClass);

        return lookedUpClass;
    }
}
