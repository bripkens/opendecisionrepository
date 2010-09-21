package nl.rug.search.odr;


import java.io.Serializable;
import java.util.List;

/**
 *
 * @param <T>
 * @param <ID> 
 * @author cm
 * @modified ben
 */
public interface GenericDaoLocal<T, ID extends Serializable> {

    T getById(ID id);

    List<T> getAll();

    void makeTransient(T entity);

    boolean isPersistable(T entity);
}
