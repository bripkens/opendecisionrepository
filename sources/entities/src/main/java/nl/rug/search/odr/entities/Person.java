package nl.rug.search.odr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nl.rug.search.odr.EmailValidator;
import nl.rug.search.odr.PasswordEnryptor;


/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long personId;



    @Column(length = 30, nullable = false, unique = true, updatable = false)
    private String name;



    @Column(length = 255, nullable = false, unique = true)
    private String email;



    @Column(length = 255, nullable = false)
    private String password;



    @OneToMany(mappedBy = "person")
    private Collection<ProjectMember> memberships;



    public Person() {
        memberships = new ArrayList<ProjectMember>();
    }







    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        checkEmail(email);

        this.email = email;
    }

    private void checkEmail(String email) {
        if (!EmailValidator.isValidEmailAddress(email)) {
            throw new RuntimeException("Please provide a valid email address");
        }
    }






    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }









    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);

        this.name = name;
    }

    private void checkName(String name) {
        if (name == null) {
            throw new NullPointerException("Please provide a valid name.");
        } else if (name.trim().isEmpty()) {
            throw new RuntimeException("Please provide a valid name.");
        }
    }








    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        checkPassword(password);
        this.password = password;
    }

    public void setPlainPassword(String password) {
        checkPassword(password);
        setPassword(PasswordEnryptor.encryptPassword(password));
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new NullPointerException("Please provide a valid password.");
        } else if (password.trim().isEmpty()) {
            throw new RuntimeException("Please provide a valid password.");
        }
    }

    public boolean validatePassword(String password) {
        return PasswordEnryptor.checkPassword(password, this.password);
    }






    public Collection<ProjectMember> getMemberships() {
        return Collections.unmodifiableCollection(memberships);
    }

    public void setMemberships(Collection<ProjectMember> memberships) {
        if (memberships == null) {
            throw new NullPointerException("Memberships may not be null.");
        }

        this.memberships = memberships;
    }

    public void addMembership(ProjectMember member) {
        member.setPerson(this);
    }

    public void removeMembership(ProjectMember member) {
        member.setPerson(null);
    }

    void internalAddMembership(ProjectMember member) {
        memberships.add(member);
    }

    void internalRemoveMembership(ProjectMember member) {
        memberships.remove(member);
    }




    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personId != null ? personId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.personId == null && other.personId != null)
                || (this.personId != null && !this.personId.equals(other.personId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Person{" + "personId=" + personId + '}';
    }
}
