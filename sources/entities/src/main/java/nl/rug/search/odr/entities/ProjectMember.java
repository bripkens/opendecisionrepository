
package nl.rug.search.odr.entities;

import javax.persistence.CascadeType;
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
public class ProjectMember extends BaseEntity<ProjectMember> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.REFRESH)
    private Person person;

    @ManyToOne
    private Project project;

    @ManyToOne
    private StakeholderRole role;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        if (project == null) {
            this.project.internalRemoveMember(this);
        }

        this.project = project;

        if (project != null) {
            project.internalAddMember(this);
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
        if (person == null) {
            this.person.internalRemoveMembership(this);
        }

        this.person = person;

        if (person != null) {
            person.internalAddMembership(this);
        }
    }


    public boolean isPersistable() {
        // shouldn't be persisted this way
        return false;
    }

    boolean internalIsPersistable(Project project) {
        return person != null && role != null && project.equals(project);
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[] {person, project, role};
    }
}
