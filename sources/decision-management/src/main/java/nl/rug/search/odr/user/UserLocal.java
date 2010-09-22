
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

    boolean isUsed(String email);

    public Person tryLogin(String name, String password);

    public java.util.Collection<nl.rug.search.odr.entities.Person> getProposedPersons(java.lang.String name);

    public nl.rug.search.odr.entities.Person getByName(java.lang.String name);
}
