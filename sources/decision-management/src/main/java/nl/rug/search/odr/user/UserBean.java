package nl.rug.search.odr.user;

import java.util.Collection;
import java.util.Collections;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class UserBean extends GenericDaoBean<Person, Long> implements UserLocal {

    @PersistenceContext
    private EntityManager entityManager;




    @Override
    public boolean isPersistable(Person p) {
        if (p == null || !p.isPersistable() || isRegistered(p.getName()) || isUsed(p.getEmail())) {
            return false;
        }

        return true;
    }




    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void register(Person p) {
        if (!isPersistable(p)) {
            throw new BusinessException("Can't persist the person.");
        }

        entityManager.persist(p);
    }









    public Person getPerson(long id) {
        return entityManager.getReference(Person.class, id);
    }








    @Override
    public boolean isRegistered(String name) {
        StringValidator.isValid(name);

        name = name.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Person p WHERE LOWER(p.name) = :name");
        q.setParameter("name", name);

        long result = (Long) q.getSingleResult();
        return result != 0;
    }






    @Override
    public boolean isUsed(String email) {
        StringValidator.isValid(email);

        email = email.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Person p WHERE LOWER(p.email) = :email");
        q.setParameter("email", email);

        long result = (Long) q.getSingleResult();
        return result != 0;
    }






    @Override
    public Person tryLogin(String name, String password)  {
        StringValidator.isValid(name);
        StringValidator.isValid(password);

        name = name.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT p FROM Person p WHERE LOWER(p.name) = :name");
        q.setParameter("name", name);

        Person result;

        try {
        result = (Person) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

        if (result == null || !result.validatePassword(password)) {
            return null;
        }

        return result;
    }



    @Override
    public Collection<Person> getProposedPersons(String name) {
        name = name.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT p FROM Person p WHERE LOWER(p.name) like :name");
        q.setParameter("name", "%".concat(name).concat("%"));

        return q.getResultList();
    }


}