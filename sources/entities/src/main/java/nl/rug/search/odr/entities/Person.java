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
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.EmailValidator;
import nl.rug.search.odr.PasswordEnryptor;
import nl.rug.search.odr.StringValidator;


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
            throw new BusinessException("Please provide a valid email address");
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
        if (this.name != null) {
            throw new BusinessException("Can't change the user name");
        }

        StringValidator.isValid(name);

        name = name.trim();

        if (name.length() <= 2 || name.length() >= 30) {
            throw new BusinessException("Name is too long or too short.");
        }

        this.name = name;
    }








    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        StringValidator.isValid(password);

        this.password = password;
    }

    public void setPlainPassword(String password) {
        StringValidator.isValid(password);
        
        setPassword(PasswordEnryptor.encryptPassword(password));
    }

    public boolean validatePassword(String password) {
        return PasswordEnryptor.checkPassword(password, this.password);
    }






    public Collection<ProjectMember> getMemberships() {
        return Collections.unmodifiableCollection(memberships);
    }

    public void setMemberships(Collection<ProjectMember> memberships) {
        if (memberships == null) {
            throw new BusinessException("Memberships may not be null.");
        }

        this.memberships = memberships;
    }

    public void addMembership(ProjectMember member) {
        if (member == null) {
            throw new BusinessException("Trying to add a member which is null.");
        }

        member.setPerson(this);
    }

    public void removeMembership(ProjectMember member) {
        if (member == null) {
            throw new BusinessException("Trying to remove a member which is null.");
        }

        member.setPerson(null);
    }

    void internalAddMembership(ProjectMember member) {
        memberships.add(member);
    }

    void internalRemoveMembership(ProjectMember member) {
        memberships.remove(member);
    }









    public boolean isPersistable() {
        return name != null && password != null && email != null;
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
