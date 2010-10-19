package nl.rug.search.odr;


import java.io.Serializable;
import java.util.List;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @param <T>
 * @param <ID> 
 * @author cm
 * @modified ben
 */
public interface GenericDaoLocal<T extends BaseEntity, ID extends Serializable> {

    T getById(ID id);

    List<T> getAll();

    void makeTransient(T entity);

    void persist(T entity);

    boolean isPersistable(T entity);

    void merge(T entity);
}
