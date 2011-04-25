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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.EmailValidator;
import nl.rug.search.odr.PasswordEnryptor;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@NamedQueries(value = {
    @NamedQuery(name = "Person.getAll",
                query = "SELECT p FROM Person p"),
    @NamedQuery(name = Person.NAMED_QUERY_GET_BY_NAME,
                query = "SELECT p FROM Person p WHERE LOWER(p.name) = :name"),
    @NamedQuery(name = Person.NAMED_QUERY_GET_BY_EMAIL,
                query = "SELECT p FROM Person p WHERE LOWER(p.email) = :email"),
    @NamedQuery(name = Person.NAMED_QUERY_IS_REGISTERED,
                query = "SELECT COUNT(p) FROM Person p WHERE LOWER(p.name) = :name"),
    @NamedQuery(name = Person.NAMED_QUERY_IS_USED_FOR_FULL_REGISTRATION,
                query = "SELECT COUNT(p) FROM Person p WHERE LOWER(p.email) = :email AND p.password IS NOT NULL"),
    @NamedQuery(name = Person.NAMED_QUERY_IS_USED_OVERALL,
                query = "SELECT COUNT(p) FROM Person p WHERE LOWER(p.email) = :email"),
    @NamedQuery(name = Person.NAMED_QUERY_TRY_LOGIN,
                query = "SELECT p FROM Person p WHERE LOWER(p.email) = :email AND p.password IS NOT NULL"),
    @NamedQuery(name = Person.NAMED_QUERY_GET_PROPOSED_PERSONS,
                query = "SELECT p FROM Person p WHERE LOWER(p.name) like :name")
})
@Entity
public class Person extends BaseEntity<Person> {

    private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_GET_BY_NAME = "Person.getByName";

    public static final String NAMED_QUERY_GET_BY_EMAIL = "Person.getByEmail";

    public static final String NAMED_QUERY_IS_REGISTERED = "Person.isRegistered";

    public static final String NAMED_QUERY_IS_USED_FOR_FULL_REGISTRATION = "Person.isUsedForFullRegistration";

    public static final String NAMED_QUERY_IS_USED_OVERALL = "Person.isUsedOverall";

    public static final String NAMED_QUERY_TRY_LOGIN = "Person.tryLogin";

    public static final String NAMED_QUERY_GET_PROPOSED_PERSONS = "Person.getProposedPersons";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 30)
    private String name;

    @Column(length = 255,
            unique = true,
            nullable=false)
    private String email;

    @Column(length = 255,
            nullable = true)
    private String password;

    @OneToMany(mappedBy = "person",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
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
        StringValidator.isValid(name);

        name = name.trim();

        if (name.length() <= 2 || name.length() >= 30) {
            throw new BusinessException("Name is too long or too short.");
        }

        if (this.name != null && !this.name.equalsIgnoreCase(name)) {
            throw new BusinessException("Chaning the name is not possible.");
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
        return new Object[]{name, email, password, memberships.size()};
    }
}
