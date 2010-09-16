/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.backing;

import javax.ejb.EJB;
import nl.rug.search.odr.UserLocal;
import nl.rug.search.odr.entities.Person;

/**
 *
 * @author Stefan
 */
public class RegisterUserBackingBean {

    @EJB
    private UserLocal user;

    private String username;
        private String password;
    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void register(){
        Person person = new Person();
        person.setName(username);
        person.setPassword(password);
        person.setEmail(emailAddress);

        user.registerPerson(person);
    }
}
