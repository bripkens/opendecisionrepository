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
 * @modified ben
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
        this.entityName = entityType.getSimpleName();
    }




    @Override
    public T getById(ID id) {
        T entity = null;
        try {
            entity = manager.find(entityType, id);
        } catch (EntityNotFoundException e) {
        }

        return entity;
    }




    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return (List<T>) manager.createQuery("select e from " + getEntityName() + " as e").
                getResultList();
    }




    @Override
    public void merge(T entity) {
        entity = manager.merge(entity);
    }




    @Override
    public void makeTransient(T entity) {
        entity = manager.merge(entity);
        manager.remove(entity);
    }


    public void delete(T entity) {
        manager.remove(manager.find(entityType, entity.getId()));
    }



    public Class<T> getEntityType() {
        return entityType;
    }




    public String getEntityName() {
        return entityName;
    }




    @Override
    public void persist(T entity) {
        if (entity.isPersistable()) {
            manager.persist(entity);
        } else {
            throw new BusinessException("Entity is not persistable.");
        }
    }
}
