/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
@Entity
public class Requirement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requirementId;

    @Column(length=10000, nullable = false, unique = false, updatable = true)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        StringValidator.isValid(description);
        this.description = description;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long id) {
        if(id == null){
            throw new NullPointerException("please prove a id");
        }
        this.requirementId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requirementId != null ? requirementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Requirement)) {
            return false;
        }
        Requirement other = (Requirement) object;
        if ((this.requirementId == null && other.requirementId != null) || (this.requirementId != null && !this.requirementId.equals(other.requirementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Requirements{" + "requirementId=" + requirementId + "description=" + description + '}';
    }

}
