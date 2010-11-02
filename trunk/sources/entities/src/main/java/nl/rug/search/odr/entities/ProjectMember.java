package nl.rug.search.odr.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@NamedQueries(value = {
    @NamedQuery(name = "ProjectMember.getAll",
                query = "SELECT pm FROM ProjectMember pm"),
    @NamedQuery(name = ProjectMember.NAMED_QUERY_GET_ALL_MEMBERS_FROM_PROJECT,
                query = "SELECT pm FROM ProjectMember pm WHERE pm.project.id = :projectId")
})
@Entity
public class ProjectMember extends BaseEntity<ProjectMember> {

    private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_GET_ALL_MEMBERS_FROM_PROJECT = "ProjectMember.getAllMembersFromProject";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Person person;

    @ManyToOne
    private Project project;

    @ManyToOne
    private StakeholderRole role;

    private boolean removed;




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
        if (id == null) {
            throw new BusinessException("Id is null");
        }

        this.id = id;
    }




    public StakeholderRole getRole() {
        return role;
    }




    public void setRole(StakeholderRole role) {
        if (role == null) {
            throw new BusinessException("Role is null");
        }

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




    @Override
    public boolean isPersistable() {
        return person != null && project != null && role != null;
    }




    public boolean isRemoved() {
        return removed;
    }




    public void setRemoved(boolean removed) {
        this.removed = removed;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{person, project, role, removed};
    }
}
