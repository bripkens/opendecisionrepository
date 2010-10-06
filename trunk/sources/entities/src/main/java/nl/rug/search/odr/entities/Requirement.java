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
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
//@Entity
public class Requirement extends BaseEntity<Requirement> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column
    private String description;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        StringValidator.isValid(description);
        this.description = description;
    }

    @Override
    public String toString() {
        return "Requirements{" + "requirementId=" + id + "description=" + description + '}';
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{description};
    }

    @Override
    public boolean isPersistable() {
        if(StringValidator.isValid(description)){
            return false;
        }
        return true;
    }
}
