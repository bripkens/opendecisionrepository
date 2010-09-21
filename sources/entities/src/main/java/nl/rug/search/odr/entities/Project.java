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
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @Column(length = 50, nullable = false, unique = true, updatable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "project")
    private Collection<ProjectMember> members;

    public Project() {
        members = new ArrayList<ProjectMember>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            description = description.trim();
        }
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null) {
            throw new BusinessException("A project name may not be changed.");
        }

        StringValidator.isValid(name);

        name = name.trim();

        if (name.length() <= 2 || name.length() > 50) {
            throw new BusinessException("Name is too long or too short.");
        }

        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        if (projectId == null) {
            throw new BusinessException("Id is null");
        }

        this.projectId = projectId;
    }

    public Collection<ProjectMember> getMembers() {
        return Collections.unmodifiableCollection(members);
    }

    public void setMembers(Collection<ProjectMember> members) {
        if (members == null) {
            throw new BusinessException("Collection of members is null");
        }
        
        this.members = members;
    }

    public void addMember(ProjectMember member){
        if(member == null){
            throw new BusinessException("Member is null.");
        }
        
        member.setProject(this);
    }

    public void removeMember(ProjectMember member) {
        if(member == null){
            throw new BusinessException("Member is null.");
        }

        member.setProject(null);
    }

    void internalAddMember(ProjectMember member) {
        members.add(member);
    }

    void internalRemoveMember(ProjectMember member) {
        members.remove(member);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if (this.projectId != other.projectId && (this.projectId == null || !this.projectId.equals(other.projectId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.projectId != null ? this.projectId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Project{" + "projectId=" + projectId + '}';
    }

    public boolean isPersistable() {
        if (name == null || members.isEmpty()) {
            return false;
        }



        for(ProjectMember m : members) {
            if (!m.internalIsPersistable(this)) {
                return false;
            }
        }

        return true;
    }
}
