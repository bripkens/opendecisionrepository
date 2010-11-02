/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Stefan
 * @modified Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Version.getAll",
                query= "SELECT v FROM Version v")
})
@Entity
public class Version extends BaseEntity<Version> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date documentedWhen;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date decidedWhen;

    private boolean removed;

    @ManyToOne
    private State state;

    @ManyToMany
    private Collection<Requirement> requirements;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<Relationship> relationships;

    @ManyToMany
    private Collection<ProjectMember> initiators;




    public Version() {
        requirements = new ArrayList<Requirement>();
        relationships = new ArrayList<Relationship>();
        initiators = new ArrayList<ProjectMember>();
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




    public Date getDecidedWhen() {
        return decidedWhen;
    }




    public void setDecidedWhen(Date decidedWhen) {
        if (decidedWhen == null) {
            throw new BusinessException("decidedWhen is null");
        }

        this.decidedWhen = decidedWhen;
    }




    public Date getDocumentedWhen() {
        return documentedWhen;
    }




    public void setDocumentedWhen(Date documentedWhen) {
        if (documentedWhen == null) {
            throw new BusinessException("documentedWhen is null");
        }

        this.documentedWhen = documentedWhen;
    }




    public boolean isRemoved() {
        return removed;
    }




    public void setRemoved(boolean removed) {
        this.removed = removed;
    }




    public State getState() {
        return state;
    }




    public void setState(State state) {
        if (state == null) {
            throw new BusinessException("The state can not be null");
        }

        this.state = state;
    }




    public Collection<Requirement> getRequirements() {
        return Collections.unmodifiableCollection(requirements);
    }




    public void setRequirements(Collection<Requirement> requirements) {
        if (requirements == null) {
            throw new BusinessException("Requirements is null");
        }
        this.requirements = requirements;
    }




    public void addRequirement(Requirement requirement) {
        if (requirement == null) {
            throw new BusinessException("Requirement is null");
        }

        this.requirements.add(requirement);
    }




    public void removeRequirement(Requirement requirement) {
        if (requirement == null) {
            throw new BusinessException("Requirement is null");
        }

        this.requirements.remove(requirement);
    }




    public void removeAllRequirements() {
        requirements.clear();
    }




    public Collection<Relationship> getRelationships() {
        return Collections.unmodifiableCollection(relationships);
    }




    public void setRelationships(Collection<Relationship> relationships) {
        if (relationships == null) {
            throw new BusinessException("Relationships is null");
        }

        this.relationships = relationships;
    }




    public void addRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new BusinessException("Relationship is null");
        }

        relationships.add(relationship);
    }




    public void removeRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new BusinessException("Relationship is null");
        }

        relationships.remove(relationship);
    }




    public void removeAllRelationships() {
        relationships.clear();
    }




    public Collection<ProjectMember> getInitiators() {
        return Collections.unmodifiableCollection(initiators);
    }




    public void setInitiators(Collection<ProjectMember> initiators) {
        if (initiators == null) {
            throw new BusinessException("Initiators is null");
        }

        this.initiators = initiators;
    }




    public void addInitiator(ProjectMember initiator) {
        if (initiator == null) {
            throw new BusinessException("Initiator is null");
        }

        this.initiators.add(initiator);
    }




    public void removeInitiator(ProjectMember initiator) {
        if (initiator == null) {
            throw new BusinessException("Initiator is null");
        }

        this.initiators.remove(initiator);
    }




    public void removeAllInitiators() {
        initiators.clear();
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{documentedWhen, decidedWhen, removed, state};
    }




    @Override
    public boolean isPersistable() {
        return documentedWhen != null && decidedWhen != null && state != null && !initiators.isEmpty();
    }
}
