/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
//@Entity
public class RelationshipType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long relationshipTypeId;

    @Column(length = 30,
            nullable = false,
            updatable = true)
    private String name;

    @Column(nullable = false,
            updatable = true)
    private boolean common;




    public Long getId() {
        return relationshipTypeId;
    }




    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.relationshipTypeId = id;
    }




    public boolean isCommon() {
        return common;
    }




    public void setCommon(boolean common) {
        this.common = common;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }




    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(relationshipTypeId).
                append(name).
                append(common).
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

        RelationshipType rt = (RelationshipType) object;
        return new EqualsBuilder().append(relationshipTypeId, rt.relationshipTypeId).
                append(name, rt.name).
                append(common, rt.common).
                isEquals();
    }




    @Override
    public String toString() {
        return "RelationshipType{" + "relationshipTypeId=" + relationshipTypeId + "name=" + name + '}';
    }
}
