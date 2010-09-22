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

/**
 *
 * @author cm
 * @modified ben
 */
public abstract class GenericDaoBean<T, ID extends Serializable> implements GenericDaoLocal<T, ID> {

    @PersistenceContext
    private EntityManager manager;
    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public GenericDaoBean() {
        //find superclass of type GenericDaoBean
        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T getById(ID id) {
        T entity = null;
        try {
            entity = manager.getReference(entityType, id);
        } catch (EntityNotFoundException e) {
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return (List<T>) manager.createQuery("select e from "+ getEntityType().getSimpleName()+" as e").getResultList();
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


    public Class<T> getEntityType() {
        return entityType;
    }
}
