
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
public class User implements UserLocal {

    @PersistenceContext
    private EntityManager entityManager;

    public void registerPerson(Person p) {
        entityManager.persist(p);
    }

}
