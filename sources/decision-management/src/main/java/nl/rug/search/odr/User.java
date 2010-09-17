
package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class User implements UserLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void registerPerson(Person p) {
        entityManager.persist(p);
    }

    @Override
    public boolean isRegistered(String name) {
        name = name.trim().toLowerCase();
        
        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Person p WHERE LOWER(p.name) = :name");
        q.setParameter("name", name);

        long result = (Long) q.getSingleResult();
        return result != 0;
    }

    @Override
    public boolean isUsed(String email) {
        email = email.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Person p WHERE LOWER(p.email) = :email");
        q.setParameter("email", email);

        long result = (Long) q.getSingleResult();
        return result != 0;
    }
}
