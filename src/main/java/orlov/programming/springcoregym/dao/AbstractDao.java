package orlov.programming.springcoregym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

public abstract class AbstractDao<T, ID> implements Dao<T, ID>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        T entity = findById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(getEntityClass(), id);
    }

    public List<T> findAll() {
        String query = "SELECT e FROM " + getEntityClass().getSimpleName() + " e";
        return entityManager.createQuery(query, getEntityClass()).getResultList();
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected abstract Class<T> getEntityClass();
}
