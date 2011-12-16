package nl.rug.search.odr.ws;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.odr.EjbLookupException;

/**
 * This class only exists as EJB injection is not working for the existing
 * ODR project set-up (CDI can't be activated for the decision-management)
 * component.
 * 
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public final class SessionBeanRepository {

    private static final String JNDI_PREFIX = "java:app/DecisionManagement/";
    
    private final InitialContext context;
    
    private SessionBeanRepository() {
        try {
            context = new InitialContext();
        } catch (NamingException ex) {
            throw new EjbLookupException(ex);
        }
    }

    public static SessionBeanRepository getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    public <T> T lookup(Class<T> expected) {
        try {
            String jndi = JNDI_PREFIX + expected.getSimpleName()
                    .replaceAll("Local", "Bean") + "!" + expected.getName();
            return (T) context.lookup(jndi);
        } catch (NamingException ex) {
            throw new EjbLookupException(ex);
        }
    }
    
    private static final class InstanceHolder {

        private InstanceHolder() {
        }
        
        private static final SessionBeanRepository INSTANCE =
                new SessionBeanRepository();
    }
}
