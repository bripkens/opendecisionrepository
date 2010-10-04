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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
@Entity
public class ActionType extends BaseEntity<ActionType> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Column
    private boolean common;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        if(id == null){
         throw new BusinessException("ActionType Id is null");
        }
        this.id = id;
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
    public String toString() {
        return "ActionType{ type=" + name + '}';
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, common};
    }

    public boolean isPersistable() {
        if(name == null){
            return false;
        }
        return true;
    }


}
