
package nl.rug.search.odr.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

    @Column(length=30,nullable=false,unique=true,updatable=false)
    private String name;
    
    @Column(length=255,nullable=false,unique=true)
    private String email;

    @Column(length=255,nullable=false)
    private String password;

    @OneToMany(mappedBy="person")
    private Collection<ProjectMember> memberShips;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<ProjectMember> getMemberShips() {
        return memberShips;
    }

    public void setMemberShips(Collection<ProjectMember> memberShips) {
        this.memberShips = memberShips;
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
        if ((this.personId == null && other.personId != null) ||
                (this.personId != null && !this.personId.equals(other.personId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Person{" + "personId=" + personId + '}';
    }

}
