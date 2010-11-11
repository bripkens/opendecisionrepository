package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Equals compares primary keys if both are non-null.
 * If either is null, then the class must define the fields that determine
 * uniqueness and compare those.
 *
 * hashCode always uses the same fields used in the equals comparison and never
 * the primary key. If the entity is immutable or the fields that are used for
 * the calculation are, then the hashCode could be cached to save the cost of
 * unnecessary recalculation.
 */
public abstract class BaseEntity<T extends BaseEntity<T>> implements Serializable {

    public abstract Long getId();
    public abstract void setId(Long id);

    public abstract boolean isPersistable();

    @Override
    public final int hashCode() {
        return calculateHashCode(getCompareData());
    }

    @Override
    public final boolean equals(final Object other) {

        if (other==null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (! (other instanceof BaseEntity)) {
            return false;
        }

        // if pks are both set, compare
        if (getId() != null) {
            Serializable otherPk = ((BaseEntity) other).getId();
            if (otherPk != null) {
                return getId().equals(otherPk);
            }
        }

        //else compare data
        return dataEquals((T) other);
    }

    private boolean dataEquals(final T obj2) {
        EqualsBuilder builder = new EqualsBuilder();

        Object[] compareDataObj1 = getCompareData();
        Object[] compareDataObj2 = obj2.getCompareData();

        for(int i = 0; i < compareDataObj1.length; i++) {
            builder.append(compareDataObj1[i], compareDataObj2[i]);
        }

        return builder.isEquals();
    }



    @Transient
    protected abstract Object[] getCompareData();

    protected int calculateHashCode(final Object... values) {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Object value : values) {
            builder.append(value);
        }
        return builder.toHashCode();
    }


    public String getEntityName() {
        return getClass().getSimpleName();
    }
}
