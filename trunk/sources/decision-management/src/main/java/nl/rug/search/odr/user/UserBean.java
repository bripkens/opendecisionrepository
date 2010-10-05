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
import nl.rug.search.odr.EmailValidator;
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
        if (p == null || !p.isPersistable() || isRegistered(p.getName()) || isUsedForFullRegistration(p.getEmail())) {
            return false;
        }

        return true;
    }

    @Override
    public void register(Person p) {
        if (!isPersistable(p)) {
            throw new BusinessException("Can't persist the person.");
        }

        try {
            Person personInDatabase = getByEmail(p.getEmail());

            personInDatabase.setName(p.getName());
            personInDatabase.setPassword(p.getPassword());

            p.setId(personInDatabase.getId());

            entityManager.merge(personInDatabase);
        } catch (NoResultException ex) {
            entityManager.persist(p);
        }
    }

    @Override
    public Person preRegister(String email) {
        Person p = new Person();
        p.setEmail(email);

        if (isUsedOverall(email)) {
            throw new BusinessException("Email already in use!");
        }

        entityManager.persist(p);
        return p;
    }

    @Override
    public Person getByName(String name) {
        StringValidator.isValid(name);

        name = name.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT p FROM Person p WHERE LOWER(p.name) = :name");
        q.setParameter("name", name);

        return (Person) q.getSingleResult();
    }

    @Override
    public Person getByEmail(String email) {
        if (!EmailValidator.isValidEmailAddress(email)) {
            return null;
        }

        email = email.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT p FROM Person p WHERE LOWER(p.email) = :email");
        q.setParameter("email", email);

        return (Person) q.getSingleResult();
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
    public boolean isUsedForFullRegistration(String email) {
        StringValidator.isValid(email);

        email = email.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Person p WHERE LOWER(p.email) = :email AND p.password IS NOT NULL");
        q.setParameter("email", email);

        long result = (Long) q.getSingleResult();

        return result != 0;
    }

    @Override
    public boolean isUsedOverall(String email) {
        StringValidator.isValid(email);

        email = email.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT COUNT(p) FROM Person p WHERE LOWER(p.email) = :email");
        q.setParameter("email", email);

        long result = (Long) q.getSingleResult();
        return result != 0;
    }

    @Override
    public Person tryLogin(String email, String password) {
        if (!EmailValidator.isValidEmailAddress(email)) {
            throw new BusinessException("Invalid email address!");
        }
        StringValidator.isValid(password);

        email = email.trim().toLowerCase();

        Query q = entityManager.createQuery("SELECT p FROM Person p WHERE LOWER(p.email) = :email AND p.password IS NOT NULL");
        q.setParameter("email", email);

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
