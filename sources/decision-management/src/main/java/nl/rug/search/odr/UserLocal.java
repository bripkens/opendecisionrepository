
package nl.rug.search.odr;

import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface UserLocal {
    void registerPerson(Person p);

    boolean isRegistered(String name);

    boolean isUsed(String email);

    public nl.rug.search.odr.entities.Person tryLogin(java.lang.String name, java.lang.String password);
}
