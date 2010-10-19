package nl.rug.search.odr.entities;

/**
 *
 * @author Stefan
 */
public abstract class AbstractProjectEntity<T extends BaseEntity<T>> extends BaseEntity<T> {

    public abstract String getName();




    public abstract void setName(String name);




    public abstract boolean isCommon();




    public abstract void setCommon(boolean common);
}
