
package nl.rug.search.odr;

import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;

import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Ignore
public abstract class AbstractEjbTest {


    private static EJBContainer ejbC;

    @Before
    public void tearDown() {
        ejbC = ContainerHolder.container;
        DatabaseCleaner.bruteForceCleanup();
    }

    public static <T> T lookUp(Class classType, Class<T> interfaceType) {
        T lookedUpClass = null;
        
        lookedUpClass = EjbUtil.lookUp(classType, interfaceType);

        assertNotNull("Looked up class " + classType.getName() + " is null.", lookedUpClass);

        return lookedUpClass;
    }

    private static class ContainerHolder {
        private static EJBContainer container = EJBContainer.createEJBContainer();
    }
}
