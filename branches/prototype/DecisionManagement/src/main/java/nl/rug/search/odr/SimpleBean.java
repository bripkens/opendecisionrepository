
package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.SystemUser;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class SimpleBean implements SimpleBeanLocal {
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @PersistenceContext
    private EntityManager entityManager;

    public String getText() {
        SystemUser u = new SystemUser();
        u.setName("Peter");
        entityManager.persist(u);

        return "42";
    }
 
}
