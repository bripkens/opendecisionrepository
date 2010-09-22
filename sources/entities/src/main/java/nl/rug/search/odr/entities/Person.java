package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.CascadeType;
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
public class Person extends BaseEntity<Person> {

    private static final long serialVersionUID = 1L;

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @Column(length = 30, nullable = false, unique = true, updatable = false)
    private String name;



    @Column(length = 255, nullable = false, unique = true)
    private String email;



    @Column(length = 255, nullable = false)
    private String password;



    @OneToMany(mappedBy = "person", cascade=CascadeType.ALL, orphanRemoval=true)
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






    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    protected Object[] getCompareData() {
        return new Object[] {name, email, password, memberships.size()};
    }
}
