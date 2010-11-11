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

        return entityManager.createNamedQuery(Person.NAMED_QUERY_GET_BY_NAME, Person.class).
                setParameter("name", name).
                getSingleResult();
    }




    @Override
    public Person getByEmail(String email) {
        if (!EmailValidator.isValidEmailAddress(email)) {
            return null;
        }

        email = email.trim().toLowerCase();

        return entityManager.createNamedQuery(Person.NAMED_QUERY_GET_BY_EMAIL, Person.class).
                setParameter("email", email).
                getSingleResult();
    }




    @Override
    public boolean isRegistered(String name) {
        StringValidator.isValid(name);

        name = name.trim().toLowerCase();

        return entityManager.createNamedQuery(Person.NAMED_QUERY_IS_REGISTERED, Long.class).
                setParameter("name", name).
                getSingleResult() != 0;
    }




    @Override
    public boolean isUsedForFullRegistration(String email) {
        StringValidator.isValid(email);

        email = email.trim().toLowerCase();

        return entityManager.createNamedQuery(Person.NAMED_QUERY_IS_USED_FOR_FULL_REGISTRATION, Long.class).
                setParameter("email", email).
                getSingleResult() != 0;
    }




    @Override
    public boolean isUsedOverall(String email) {
        StringValidator.isValid(email);

        email = email.trim().toLowerCase();

        return entityManager.createNamedQuery(Person.NAMED_QUERY_IS_USED_OVERALL, Long.class).
                setParameter("email", email).
                getSingleResult() != 0;
    }




    @Override
    public Person tryLogin(String email, String password) {
        if (!EmailValidator.isValidEmailAddress(email)) {
            throw new BusinessException("Invalid email address!");
        }
        StringValidator.isValid(password);

        email = email.trim().toLowerCase();

        Person result;

        try {
            result = entityManager.createNamedQuery(Person.NAMED_QUERY_TRY_LOGIN, Person.class).
                    setParameter("email", email).
                    getSingleResult();
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

        return entityManager.createNamedQuery(Person.NAMED_QUERY_GET_PROPOSED_PERSONS).
                setParameter("name", "%".concat(name).concat("%")).
                getResultList();
    }
}
