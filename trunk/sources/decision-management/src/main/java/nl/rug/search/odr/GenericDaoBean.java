/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author cm
 * @author ben
 */
public abstract class GenericDaoBean<T extends BaseEntity, ID extends Serializable> implements GenericDaoLocal<T, ID> {

    @PersistenceContext
    private EntityManager manager;

    private Class<T> entityType;

    private String entityName;




    @SuppressWarnings("unchecked")
    public GenericDaoBean() {
        //find superclass of type GenericDaoBean
        this.entityType = (Class<T>) ((ParameterizedType) getClass().
                getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            this.entityName = entityType.newInstance().getEntityName();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }




    @Override
    public T getById(ID id) {
        T entity = null;
        try {
            entity = manager.find(getEntityType(), id);
        } catch (EntityNotFoundException e) {
        }

        return entity;
    }




    @Override
    public List<T> getAll() {
        return (List<T>) manager.createNamedQuery(getEntityName().concat(".getAll")).
                getResultList();
    }




    @Override
    public void merge(T entity) {
        if (entity.isPersistable()) {
            manager.merge(entity);
        } else {
            throw new BusinessException("Entity is not persistable.");
        }
    }




    @Override
    public void makeTransient(T entity) {
        manager.detach(entity);
    }




    @Override
    public void delete(T entity) {
        manager.remove(manager.find(getEntityType(), entity.getId()));
    }




    protected Class<T> getEntityType() {
        return entityType;
    }




    protected String getEntityName() {
        return entityName;
    }


    
    protected EntityManager getEntityManager() {
        return manager;
    }
    


    @Override
    public void persist(T entity) {
        if (isPersistable(entity)) {
            manager.persist(entity);
        } else {
            throw new BusinessException("Entity is not persistable.");
        }
    }
}
