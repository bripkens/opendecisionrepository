/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import nl.rug.search.odr.StringValidator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
//@Entity
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long VersionId;
    @Column(nullable = false, unique = true, updatable = false)
    private int revision;
    @Column(nullable = false, unique = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createDate;
    @OneToOne
    private Action action;
    @ManyToOne
    private Status status;
    @OneToMany
    private Requirement requirement;
    @ManyToMany(mappedBy = "version")
    private Collection<Relationship> relationships;

    public Version() {
        relationships = new ArrayList<Relationship>();
    }

    public Long getVersionId() {
        return VersionId;
    }

    public void setVersionId(Long id) {
        if (id == null) {
            throw new NullPointerException("please provide an id");
        }
        this.VersionId = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (action == null) {
            throw new NullPointerException("Please provide a action");
        }
        this.action = action;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    public Collection<Relationship> getRelationships() {
        return Collections.unmodifiableCollection(relationships);
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        if (requirement == null) {
            throw new NullPointerException("Please provide a requirment");
        }
        this.requirement = requirement;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        if (revision < 0) {
            throw new RuntimeException("Please provide a revision >= 0 ");
        }
        this.revision = revision;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new NullPointerException("Please provide a status");
        }
        this.status = status;
    }

    public void addRelationship(Relationship ship) {
        if (ship == null) {
            throw new NullPointerException("please provide a relationship");
        }
        ship.setSourceVersion(this);
    }

    public void removeRelationShip(Relationship ship) {
        ship.setSourceVersion(null);
    }

    void internalAddRelationship(Relationship ship) {
        relationships.add(ship);
    }

    void internalRemoveRelationship(Relationship ship) {
        relationships.remove(ship);
    }

    void setRelationships(Collection<Relationship> relationships) {
        if (relationships == null) {
            throw new NullPointerException("relationships may not be null.");
        }

        this.relationships = relationships;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(VersionId).
                append(revision).
                append(createDate).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }

        Version v2 = (Version) object;
        return new EqualsBuilder().append(VersionId, v2.VersionId).
                append(revision, v2.revision).
                append(createDate, v2.createDate).
                isEquals();
    }

    @Override
    public String toString() {
        return "Version{" + "VersionId=" + VersionId + "revision=" + revision + "createDate=" + createDate + '}';
    }
}
