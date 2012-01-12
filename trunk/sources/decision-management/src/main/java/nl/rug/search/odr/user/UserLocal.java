package nl.rug.search.odr.user;

import java.util.Collection;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface UserLocal extends GenericDaoLocal<Person, Long> {

    void register(Person p);

    boolean isRegistered(String name);

    boolean isUsedForFullRegistration(String email);

    Person tryLogin(String email, String password);

    Collection<Person> getProposedPersons(String name);

    Person getByName(String name);

    Person getByEmail(String email);

    Person preRegister(String email);

    boolean isUsedOverall(String email);

    Person getByEmailOrNull(String email);
}
