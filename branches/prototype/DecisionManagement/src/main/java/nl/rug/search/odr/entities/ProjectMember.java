
package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class ProjectMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectMemberId;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Project project;

    @ManyToOne
    private StakeholderRole role;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getProjectMemberId() {
        return projectMemberId;
    }

    public void setProjectMemberId(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
    }

    public StakeholderRole getRole() {
        return role;
    }

    public void setRole(StakeholderRole role) {
        this.role = role;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectMemberId != null ? projectMemberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectMember)) {
            return false;
        }
        ProjectMember other = (ProjectMember) object;
        if ((this.projectMemberId == null && other.projectMemberId != null) ||
                (this.projectMemberId != null && !this.projectMemberId.equals(other.projectMemberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProjectMember{" + "projectMemberId=" + projectMemberId + '}';
    }

    

}
