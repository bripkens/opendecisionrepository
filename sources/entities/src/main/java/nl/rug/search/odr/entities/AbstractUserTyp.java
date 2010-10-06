/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.entities;




/**
 *
 * @author Stefan
 */
public abstract class AbstractUserTyp <T extends BaseEntity<T>> extends BaseEntity<T> {

    public abstract String getName();
    public abstract void setName(String name);

    public abstract boolean isCommon();
    public abstract void setCommon(boolean common);
}
