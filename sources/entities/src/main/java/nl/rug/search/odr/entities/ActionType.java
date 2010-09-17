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
//@Entity
public class ActionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long actionTypeId;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Column
    private boolean common;

    public Long getId() {
        return actionTypeId;
    }

    public void setId(Long id) {
        if(id == null){
         throw new NullPointerException("is is null");
        }
        this.actionTypeId = id;
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
        int hash = 0;
        hash += (actionTypeId != null ? actionTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionType)) {
            return false;
        }
        ActionType other = (ActionType) object;
        if ((this.actionTypeId == null && other.actionTypeId != null) || (this.actionTypeId != null && !this.actionTypeId.equals(other.actionTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ActionType{" + "actionTypeId=" + actionTypeId + "name=" + name + "common=" + common + '}';
    }


}
