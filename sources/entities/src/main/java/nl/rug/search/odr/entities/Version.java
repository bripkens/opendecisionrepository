/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Stefan
 * @modified Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Version.getAll",
    query = "SELECT v FROM Version v")
})
@Entity
public class Version extends BaseEntity<Version> {

    private static final long serialVersionUID = 1L;
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL})
    @ManyToOne
    private Decision decision;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date documentedWhen;
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date decidedWhen;
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL})
    private boolean removed;
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL})
    @ManyToOne
    private State state;
    @ManyToMany
    private Collection<Concern> concerns;
    @OneToMany(cascade = CascadeType.ALL,
    mappedBy = "source",
    orphanRemoval = true)
    private Collection<Relationship> outgoingRelationships;
    @OneToMany(cascade = CascadeType.ALL,
    mappedBy = "target",
    orphanRemoval = true)
    private Collection<Relationship> incomingRelationships;
    @ManyToMany
    private Collection<ProjectMember> initiators;

    public Version() {
        concerns = new ArrayList<Concern>();
        outgoingRelationships = new ArrayList<Relationship>();
        incomingRelationships = new ArrayList<Relationship>();
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

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        if (this.decision != null) {
            this.decision.internalRemoveVersion(this);
        }

        this.decision = decision;

        if (decision != null) {
            decision.internalAddVersion(this);
        }
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

    public Collection<Concern> getConcerns() {
        return Collections.unmodifiableCollection(concerns);
    }

    public void setConcerns(Collection<Concern> concerns) {
        if (concerns == null) {
            throw new BusinessException("Concernlist is null");
        }
        this.concerns = concerns;
    }

    public void addConcern(Concern concern) {
        if (concern == null) {
            throw new BusinessException("Concern is null");
        }

        this.concerns.add(concern);
    }

    public void removeConcern(Concern concern) {
        if (concern == null) {
            throw new BusinessException("Concern is null");
        }

        this.concerns.remove(concern);
    }

    public void removeAllConcerns() {
        concerns.clear();
    }

    public Collection<Relationship> getOutgoingRelationships() {
        return Collections.unmodifiableCollection(outgoingRelationships);
    }

    public void setOutgoingRelationships(Collection<Relationship> relationships) {
        if (relationships == null) {
            throw new BusinessException("Relationships is null");
        }

        this.outgoingRelationships = relationships;
    }

    public void addOutgoingRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new BusinessException("Relationship is null");
        }

        relationship.setSource(this);
    }

    void internalAddOutgoingRelationship(Relationship relationship) {
        outgoingRelationships.add(relationship);
    }

    public void removeOutgoingRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new BusinessException("Relationship is null");
        }

        relationship.setSource(null);
    }

    void internalRemoveOutgoingRelationship(Relationship relationship) {
        outgoingRelationships.remove(relationship);
    }

    public void removeAllOutgoingRelationships() {
        outgoingRelationships.clear();
    }

    public Collection<Relationship> getIncomingRelationships() {
        return Collections.unmodifiableCollection(incomingRelationships);
    }

    public void setIncomingRelationships(Collection<Relationship> relationships) {
        if (relationships == null) {
            throw new BusinessException("Relationships is null");
        }

        this.incomingRelationships = relationships;
    }

    public void addIncomingRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new BusinessException("Relationship is null");
        }

        relationship.setTarget(this);
    }

    void internalAddIncomingRelationship(Relationship relationship) {
        incomingRelationships.add(relationship);
    }

    public void removeIncomingRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new BusinessException("Relationship is null");
        }

        relationship.setTarget(null);
    }

    void internalRemoveIncomingRelationship(Relationship relationship) {
        incomingRelationships.remove(relationship);
    }

    public void removeAllIncomingRelationships() {
        incomingRelationships.clear();
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

    public static class DecidedWhenComparator implements Comparator<Version> {

        @Override
        public int compare(Version o1, Version o2) {
            return o1.decidedWhen.compareTo(o2.decidedWhen);
        }
    }
}
