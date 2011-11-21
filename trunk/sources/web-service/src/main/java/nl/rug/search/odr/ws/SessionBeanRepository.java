package nl.rug.search.odr.ws;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.user.UserLocal;

/**
 * This class only exists as EJB injection is not working for the existing
 * ODR project set-up (CDI can't be activated for the decision-management)
 * component.
 * 
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class SessionBeanRepository {

    private static final String JNDI_PREFIX = "java:comp/env/";
    
    private final InitialContext context;
    
    private SessionBeanRepository() {
        try {
            context = new InitialContext();
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static SessionBeanRepository getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    private Object lookup(String jndi) {
        try {
            return context.lookup(JNDI_PREFIX + jndi);
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public ProjectLocal getProjectLocal() {
        return (ProjectLocal) lookup(ProjectLocalLoookupHelper.NAME);
    }
    
    public UserLocal getUserLocal() {
        return (UserLocal) lookup(UserLocalLookupHelper.NAME);
    }
    
    public StateLocal getStateLocal() {
        return (StateLocal) lookup(StateLocalLookupHelper.NAME);
    }
    
    public RelationshipTypeLocal getRelationshipTypeLocal() {
        return (RelationshipTypeLocal)
                lookup(RelationshipTypeLocalLookupHelper.NAME);
    }
    
    private static class InstanceHolder {
        private static final SessionBeanRepository INSTANCE =
                new SessionBeanRepository();
    }
    
    @EJB(name = ProjectLocalLoookupHelper.NAME,
            beanInterface = ProjectLocal.class)
    private static class ProjectLocalLoookupHelper {
        private static final String NAME = "projectLocal";
    }

    @EJB(name = UserLocalLookupHelper.NAME, beanInterface = UserLocal.class)
    private static class UserLocalLookupHelper {
        private static final String NAME = "userLocal";
    }
    
    @EJB(name = StateLocalLookupHelper.NAME, beanInterface = StateLocal.class)
    private static class StateLocalLookupHelper {
        private static final String NAME = "stateLocal";
    }
    
    @EJB(name = RelationshipTypeLocalLookupHelper.NAME,
            beanInterface = RelationshipTypeLocal.class)
    private static class RelationshipTypeLocalLookupHelper {
        private static final String NAME = "relationshipTypeLocal";
    }
}
