
package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.Person;

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
        Person u = new Person();
        u.setName("Peter");
        entityManager.persist(u);

        return "42";
    }
 
}
