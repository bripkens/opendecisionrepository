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
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class Project extends BaseEntity<Project> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50, nullable = false, unique = true, updatable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "project", cascade=CascadeType.ALL, orphanRemoval=true)
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

    @Override
    protected Object[] getCompareData() {
        return new Object[] {name, description, members.size()};
    }
}
