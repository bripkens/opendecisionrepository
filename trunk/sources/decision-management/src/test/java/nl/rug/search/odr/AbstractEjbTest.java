
package nl.rug.search.odr;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
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


    private EJBContainer ejbC;

    @Before
    public void tearUp() {
        ejbC = ContainerHolder.container;
        DatabaseCleaner.bruteForceCleanup();
    }

    public static boolean moveFile(String source, String target, boolean override) {
        File sourceFile = new File(source);
        File temp = new File(target);

        if (temp.exists()) {
            if (override) {
                temp.delete();
            } else {
                throw new RuntimeException("Destination file exists: " + target);
            }
        }

        return sourceFile.renameTo(temp);
    }

    public static <T> T lookUp(Class classType, Class<T> interfaceType) {
        T lookedUpClass = null;
        
        lookedUpClass = EjbUtil.lookUp(classType, interfaceType);

        assertNotNull("Looked up class " + classType.getName() + " is null.", lookedUpClass);

        return lookedUpClass;
    }

    private static class ContainerHolder {
        private static final EJBContainer container;

        static {
            moveFile("./target/classes/META-INF/persistence.xml",
                    "./target/classes/META-INF/persistence.xml.backup", true);
            moveFile("./target/test-classes/META-INF/test-persistence.xml",
                    "./target/classes/META-INF/persistence.xml", false);

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put("org.glassfish.ejb.embedded.glassfish.installation.root",
                    "./src/test/glassfish");
            container = EJBContainer.createEJBContainer(properties);

            moveFile("./target/classes/META-INF/persistence.xml",
                    "./target/test-classes/META-INF/test-persistence.xml", false);
            moveFile("./target/classes/META-INF/persistence.xml.backup",
                    "./target/classes/META-INF/persistence.xml", false);
        }
    }
}
