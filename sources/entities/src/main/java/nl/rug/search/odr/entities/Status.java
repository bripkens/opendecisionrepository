/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.entities;

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
public class Status extends BaseEntity<Status>{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private boolean common;

    public void setCommon(boolean common) {
        this.common = common;
    }

    public boolean isCommon(){
        return common;
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
        return "Status{" + "name=" + name + "common=" + common + '}';
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, common};
    }

    @Override
    public Long getId() {
         return id;
    }

    @Override
    public void setId(Long id) {
       if(id == null){
            throw new BusinessException("Id is null.");
        }
        this.id = id;
    }
}
