
package nl.rug.search.odr;

import nl.rug.search.odr.test.TestDeleteHelper;
import nl.rug.search.odr.test.TestDeleteHelperLocal;
import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;

import javax.naming.Context;
import org.junit.After;
import org.junit.AfterClass;
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
    private final static TestDeleteHelperLocal deleteHelper;

    static {
        ejbC = EJBContainer.createEJBContainer();

        ctx = ejbC.getContext();

        deleteHelper = EjbUtil.lookUp(TestDeleteHelper.class, TestDeleteHelperLocal.class);
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

    @After
    public void tearDown() {
        deleteHelper.deleteAll();
    }

    public static <T> T lookUp(Class classType, Class<T> interfaceType) {
        T lookedUpClass = null;
        
        lookedUpClass = EjbUtil.lookUp(classType, interfaceType);

        assertNotNull("Looked up class " + classType.getName() + " is null.", lookedUpClass);

        return lookedUpClass;
    }
}
