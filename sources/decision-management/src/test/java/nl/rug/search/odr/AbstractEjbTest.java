
package nl.rug.search.odr;

import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
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
    private final static EntityManager manager;

    static {
        ejbC = EJBContainer.createEJBContainer();

        ctx = ejbC.getContext();

        manager = Persistence.createEntityManagerFactory("decision_management_pu").createEntityManager();
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
        
        lookedUpClass = EjbUtil.lookUp(classType, interfaceType);

        assertNotNull("Looked up class " + classType.getName() + " is null.", lookedUpClass);

        return lookedUpClass;
    }

    public static void deleteRecords(String entityName) {
//        String query = "DELETE FROM " + entityName;
//
//        Query q = manager.createQuery(query);
//        q.executeUpdate();
    }
}
