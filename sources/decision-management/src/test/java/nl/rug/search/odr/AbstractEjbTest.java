
package nl.rug.search.odr;

import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;

import javax.naming.Context;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public class AbstractEjbTest {


    private final static EJBContainer ejbC;
    private final static Context ctx;
 //   private final static TestDeleteHelperLocal deleteHelper;

    static {
        ejbC = EJBContainer.createEJBContainer();

        ctx = ejbC.getContext();

        //deleteHelper = EjbUtil.lookUp(TestDeleteHelper.class, TestDeleteHelperLocal.class);
       
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

    @Before
    public void tearDown() {
//        new DatabaseCleaner().delete();
        DatabaseCleaner.bruteForceCleanup();
    }

    public static <T> T lookUp(Class classType, Class<T> interfaceType) {
        T lookedUpClass = null;
        
        lookedUpClass = EjbUtil.lookUp(classType, interfaceType);

        assertNotNull("Looked up class " + classType.getName() + " is null.", lookedUpClass);

        return lookedUpClass;
    }
}
