/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Stefan
 */
//@Entity
public class Version extends BaseEntity<Version> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int revision;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createDate;

    @ManyToOne
    private ArchitecturalDecision decision;


    @OneToOne(cascade=CascadeType.ALL)
    private Action action;

    @ManyToOne
    private Status status;

    @OneToMany
    private Collection<Requirement> requirements;

//    @ManyToMany(mappedBy = "version")
//    private Collection<Relationship> relationships;
    public ArchitecturalDecision getDecision() {
        return decision;
    }

    public void setArchitecturalDecision(ArchitecturalDecision decision) {
        if (decision == null) {
            this.decision.internalRemoveVersion(this);
        }
        this.decision = decision;

        if (decision != null) {
            decision.internalAddVersion(this);
        }
    }

    public Version() {
        requirements = new ArrayList<Requirement>();
//        relationships = new ArrayList<Relationship>();
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("is is null");
        }
        this.id = id;
    }

    public void addRequirment(Requirement re){
        if(re == null){
            throw new BusinessException("Please provide a requirement");
        }
        requirements.add(re);
    }

    public void removeRequirement(Requirement re){
        if(re == null){
            throw new BusinessException("Can't delete the Requirment null");
        }
        requirements.remove(re);
    }

    public Collection<Requirement> getRequirements(){
        return requirements;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (action == null) {
            throw new BusinessException("Paction is null");
        }
        this.action = action;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

//    public Collection<Relationship> getRelationships() {
//        return Collections.unmodifiableCollection(relationships);
//    }
//    public Requirement getRequirement() {
//        return requirement;
//    }
//
//    public void setRequirement(Requirement requirement) {
//        if (requirement == null) {
//            throw new BusinessException("requirment is null");
//        }
//        this.requirement = requirement;
//    }
    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        if (revision < 0) {
            throw new BusinessException("version is smaller than 0");
        }
        this.revision = revision;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        if (status == null) {
            throw new BusinessException("status is null");
        }
        this.status = status;
    }
    
//    public void addRelationship(Relationship ship) {
//        if (ship == null) {
//            throw new BusinessException("relationship is null");
//        }
//        ship.setSourceVersion(this);
//    }
//
//    public void removeRelationShip(Relationship ship) {
//        ship.setSourceVersion(null);
//    }
//    void internalAddRelationship(Relationship ship) {
//        relationships.add(ship);
//    }
//
//    void internalRemoveRelationship(Relationship ship) {
//        relationships.remove(ship);
//    }
//
//    void setRelationships(Collection<Relationship> relationships) {
//        if (relationships == null) {
//            throw new BusinessException("Collection relationsships is null");
//        }
//
//        this.relationships = relationships;
//    }
    @Override
    public String toString() {
        return "Version{" + "VersionId=" + id + "revision=" + revision + "createDate=" + createDate + '}';
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{revision, createDate};
    }

    @Override
    public boolean isPersistable() {
        return false;
    }
}
