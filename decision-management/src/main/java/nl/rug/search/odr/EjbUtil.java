
package nl.rug.search.odr;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class EjbUtil {

    public static <T> T lookUp(Class classType, Class<T> interfaceType) {
        T lookedUpClass = null;

        try {
            Context ctx = new InitialContext();
            lookedUpClass = (T) ctx.lookup("java:global/classes/" + classType.getSimpleName());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        
        return lookedUpClass;
    }


    public static <T> T lookUp(String jndiName, Class<T> interfaceType) {
        try {
            InitialContext ic = new InitialContext();

            return (T) ic.lookup("java:comp/env/" + jndiName);
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
