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

/**
 *
 * @author Stefan
 */
//@Entity
public class Relationship implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
        return id;
    }

    public void setId(Long id) {
        if(id == null){
            throw new NullPointerException("please provide an id");
        }
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Relationship)) {
            return false;
        }
        Relationship other = (Relationship) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Relationship{" + "id=" + id + "type=" + type + '}';
    }
}
