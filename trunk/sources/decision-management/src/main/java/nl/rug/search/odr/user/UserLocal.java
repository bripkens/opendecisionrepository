
package nl.rug.search.odr.user;

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

    public Person tryLogin(String email, String password);

    public java.util.Collection<nl.rug.search.odr.entities.Person> getProposedPersons(java.lang.String name);

    public nl.rug.search.odr.entities.Person getByName(java.lang.String name);

    public nl.rug.search.odr.entities.Person getByEmail(java.lang.String email);

    public nl.rug.search.odr.entities.Person preRegister(java.lang.String email);

    public boolean isUsedOverall(java.lang.String email);

    public nl.rug.search.odr.entities.Person getByEmailOrNull(java.lang.String email);
}
