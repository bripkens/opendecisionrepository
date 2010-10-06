/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 *
 * @author Stefan
 */
@Entity
public abstract class AbstractUserTyp extends BaseEntity<AbstractUserTyp> {
    @Id
    private Long id;

    public abstract String getName();
    public abstract void setName(String name);

    public abstract boolean isCommon();
    public abstract void setCommon(boolean common);



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
