/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
//@Entity
public class Relationship implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long RelationshipId;
    @ManyToOne
    private RelationshipType type;
    @ManyToMany
    private Version source;
    @ManyToMany
    private Version target;

    public Version getSource() {
        return source;
    }

    public Version getTarget() {
        return target;
    }

    public void setSourceVersion(Version version) {
        if (version == null) {
            this.source.internalRemoveRelationship(this);
        }
        this.source = version;

        if (version != null) {
            version.internalAddRelationship(this);
        }
    }

    public void setTargetVersion(Version version) {
        this.target = version;

    }

    public Long getId() {
        return RelationshipId;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new NullPointerException("please provide an id");
        }
        this.RelationshipId = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(RelationshipId).toHashCode();
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

        Relationship ac = (Relationship) object;
        return new EqualsBuilder().append(RelationshipId, ac.RelationshipId).
                isEquals();
    }

    @Override
    public String toString() {
        return "Relationship{" + "id=" + RelationshipId +'}';
    }
}
