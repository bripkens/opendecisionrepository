package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@NamedQueries(value = {
    @NamedQuery(name = "Project.getAll",
                query = "SELECT p FROM Project p"),
    @NamedQuery(name = Project.NAMED_QUERY_IS_NAME_USED,
                query = "SELECT COUNT(p) FROM Project p WHERE LOWER(p.name) = :name"),
    @NamedQuery(name = Project.NAMED_QUERY_GET_ALL_PROJECTS_FROM_USER,
                query = "SELECT pm FROM ProjectMember pm WHERE pm.person.id = :userId AND pm.removed = false"),
    @NamedQuery(name = Project.NAMED_QUERY_IS_MEMBER,
                query = "SELECT COUNT(pm)"
                        + " FROM ProjectMember pm "
                        + " WHERE pm.person.id = :userId AND pm.project.id = :projectId AND pm.removed = false"),
    @NamedQuery(name = Project.NAMED_QUERY_GET_BY_NAME,
                query = "SELECT p FROM Project p WHERE lower(p.name) = :name")
})
@Entity
public class Project extends BaseEntity<Project> {

    private static final long serialVersionUID = 1L;
    public static final String NAMED_QUERY_IS_NAME_USED = "Project.isNameUsed";
    public static final String NAMED_QUERY_GET_ALL_PROJECTS_FROM_USER = "Project.getAllProjectsFromUser";
    public static final String NAMED_QUERY_IS_MEMBER = "Project.isMember";
    public static final String NAMED_QUERY_GET_BY_NAME = "Project.getByName";
    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    @Column(length = 50,
            nullable = false,
            unique = true,
            updatable = false)
    private String name;
    @Column(length = 1000)
    private String description;
    @OneToMany(mappedBy = "project",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<ProjectMember> members;
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<Iteration> iterations;
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<StakeholderRole> roles;
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<State> states;
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<RelationshipType> relationshipTypes;
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<Concern> concerns;
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<Decision> decisions;
    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<RelationshipViewVisualization> relationshipViews;




    public Project() {
        members = new ArrayList<ProjectMember>();
        iterations = new ArrayList<Iteration>();
        roles = new ArrayList<StakeholderRole>();
        decisions = new ArrayList<Decision>();
        concerns = new ArrayList<Concern>();
        states = new ArrayList<State>();
        relationshipTypes = new ArrayList<RelationshipType>();
        relationshipViews = new ArrayList<RelationshipViewVisualization>();
    }




    public Collection<Iteration> getIterations() {
        return Collections.unmodifiableCollection(iterations);
    }




    public void setIterations(Collection<Iteration> iterations) {
        if (iterations == null) {
            throw new BusinessException("Iterations is null");
        }

        this.iterations = iterations;
    }




    public void addIteration(Iteration it) {
        if (it == null) {
            throw new BusinessException("iteration is null");
        }
        iterations.add(it);
    }




    public void removeIteration(Iteration it) {
        if (it == null) {
            throw new BusinessException("It is null");
        }

        iterations.remove(it);
    }




    public void removeAllIterations() {
        iterations.clear();
    }




    public void addRole(StakeholderRole role) {
        if (role == null) {
            throw new BusinessException("Please provide a role");
        }
        roles.add(role);
    }




    public void removeRole(StakeholderRole role) {
        if (role == null) {
            throw new BusinessException("Please provide a role");
        }
        roles.remove(role);
    }




    public void setRoles(Collection<StakeholderRole> roles) {
        if (roles == null) {
            throw new BusinessException("Roles are null");
        }

        this.roles = roles;
    }




    public Collection<StakeholderRole> getRoles() {
        return Collections.unmodifiableCollection(roles);
    }




    public void removeAllRoles() {
        roles.clear();
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




    public void addMember(ProjectMember member) {
        if (member == null) {
            throw new BusinessException("Member is null.");
        }

        member.setProject(this);
    }




    public void removeAllMembers() {
        members.clear();
    }




    public void removeMember(ProjectMember member) {
        if (member == null) {
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




    public Collection<Decision> getDecisions() {
        return Collections.unmodifiableCollection(decisions);
    }




    public void setDecisions(Collection<Decision> decisions) {
        if (decisions == null) {
            throw new BusinessException("Decisions is null");
        }

        this.decisions = decisions;
    }




    public void addDecision(Decision decision) {
        if (decision == null) {
            throw new BusinessException("Decision is null");
        }

        decisions.add(decision);
    }




    public void removeDecision(Decision decision) {
        if (decision == null) {
            throw new BusinessException("Decision is null");
        }

        decisions.remove(decision);
    }




    public Decision getDecision(long decisionId) {
        for (Decision decision : decisions) {
            if (decision.getId() != null && decision.getId().equals(decisionId)) {
                return decision;
            }
        }

        return null;
    }




    public void removeAllDecisions() {
        decisions.clear();
    }




    public Collection<Concern> getConcerns() {
        return Collections.unmodifiableCollection(concerns);
    }




    public void setConcerns(Collection<Concern> concerns) {
        if (concerns == null) {
            throw new BusinessException("Concern is null");
        }
        this.concerns = concerns;
    }




    public void addConcern(Concern concern) {
        if (concern == null) {
            throw new BusinessException("Concern is null");
        }

        concerns.add(concern);
    }




    public void removeConcern(Concern concern) {
        if (concern == null) {
            throw new BusinessException("Concern is null");
        }

        concerns.remove(concern);
    }




    public void removeAllConcerns() {
        concerns.clear();
    }




    public List<Concern> getDestinctConcerns() {

        if (this.concerns.isEmpty()) {
            return Collections.emptyList();
        }
        List<Concern> concernList = new ArrayList(this.concerns);

        Collections.sort(concernList, new Concern.GroupDateComparator());

        Long groupId = null;
        List<Concern> onlyNewest = new ArrayList<Concern>();

        for (Concern con : concernList) {
            if (groupId == null || !groupId.equals(con.getGroup())) {
                onlyNewest.add(con);
                groupId = con.getGroup();
            }
        }

        Collections.reverse(onlyNewest);

        return onlyNewest;
    }




    public Collection<State> getStates() {
        return Collections.unmodifiableCollection(states);
    }




    public void setStates(Collection<State> states) {
        if (states == null) {
            throw new BusinessException("States is null");
        }
        this.states = states;
    }




    public void addState(State state) {
        if (state == null) {
            throw new BusinessException("State is null");
        }

        states.add(state);
    }




    public void removeState(State state) {
        if (state == null) {
            throw new BusinessException("State is null");
        }

        states.remove(state);
    }




    public void removeAllStates() {
        states.clear();
    }




    public Collection<RelationshipType> getRelationshipTypes() {
        return Collections.unmodifiableCollection(relationshipTypes);
    }




    public void setRelationshipTypes(Collection<RelationshipType> relationshipTypes) {
        if (relationshipTypes == null) {
            throw new BusinessException("RelationshipTypes is null");
        }
        this.relationshipTypes = relationshipTypes;
    }




    public void addRelationshipType(RelationshipType relationshipType) {
        if (relationshipType == null) {
            throw new BusinessException("RelationshipType is null");
        }

        relationshipTypes.add(relationshipType);
    }




    public void removeRelationshipType(RelationshipType relationshipType) {
        if (relationshipType == null) {
            throw new BusinessException("RelationshipType is null");
        }

        relationshipTypes.remove(relationshipType);
    }




    public void removeAllRelationshipTypes() {
        relationshipTypes.clear();
    }




    public void addRelationshipView(RelationshipViewVisualization v) {
        assert v != null;

        relationshipViews.add(v);
    }




    public void removeRelationshipView(RelationshipViewVisualization v) {
        assert v != null;

        relationshipViews.remove(v);
    }




    public void setRelationshipViews(List<RelationshipViewVisualization> visualizations) {
        assert visualizations != null;

        this.relationshipViews = visualizations;
    }




    public List<RelationshipViewVisualization> getRelationshipViews() {
        return Collections.unmodifiableList(relationshipViews);
    }




    public void removeAllRelationshipViews() {
        relationshipViews.clear();
    }




    @Override
    public boolean isPersistable() {
        return name != null && !members.isEmpty();
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, description};
    }




    public void removeConcernsByGroup(Concern con) {
        Iterator<Concern> it = concerns.iterator();
        while (it.hasNext()) {
            Concern currentConcern = it.next();
            if (currentConcern.getGroup().equals(con.getGroup())) {
                it.remove();
                removeLinksToConcern(currentConcern);
            }
        }
    }

    private void removeLinksToConcern(Concern concern){
        for(Decision decision : decisions){
            for(Version version : decision.getVersions()){
                version.removeConcern(concern);
            }
        }
    }


    public static class NameComparator implements Comparator<Project> {

        @Override
        public int compare(Project o1, Project o2) {
            return o1.name.compareToIgnoreCase(o2.name);
        }
    }
}
