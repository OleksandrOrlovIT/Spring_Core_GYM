package orlov.programming.springcoregym.dao.impl.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import orlov.programming.springcoregym.dao.DaoUsernameFindable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Repository
public abstract class AbstractUserDao<E> implements DaoUsernameFindable<E> {

    @PersistenceContext
    protected EntityManager entityManager;
    protected final Class<E> entityClass;

    @Autowired
    public AbstractUserDao(EntityManager entityManager, Class<E> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    protected abstract String getNullEntityErrorMessage();

    protected abstract void checkEntityIdIsNull(Long id);

    @Override
    @Transactional
    public E create(E entity) {
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        if (getId(entity) != null) {
            throw new IllegalArgumentException("Entity's id has to be null");
        }

        entityManager.persist(entity);
        log.info("Created new {} = {}", getClass().getSimpleName(), entity);

        return entity;
    }

    @Override
    @Transactional
    public E update(E entity) {
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        checkEntityIdIsNull(getId(entity));

        if (findByObject(entity).isEmpty()) {
            IllegalArgumentException e = new IllegalArgumentException("Entity does not exist");
            log.error(e);
            throw e;
        }

        E updatedEntity = entityManager.merge(entity);
        log.info("Updating {} = {}", getClass().getSimpleName(), updatedEntity);
        return updatedEntity;
    }

    @Override
    @Transactional
    public void delete(E entity) {
        log.info("Deleting {} = {}", getClass().getSimpleName(), entity);
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        checkEntityIdIsNull(getId(entity));

        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    public List<E> findAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    @Override
    public Optional<E> findByObject(E entity) {
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        checkEntityIdIsNull(getId(entity));

        return Optional.ofNullable(entityManager.find(entityClass, getId(entity)));
    }

    @Override
    public Optional<E> findByUsername(String username) {
        Objects.requireNonNull(username, "Username can't be null");
        try {
            return Optional.of(entityManager.createQuery(
                            "SELECT u FROM " + entityClass.getSimpleName() + " u WHERE u.username = :username", entityClass)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    protected abstract Long getId(E entity);

    protected abstract void setId(E entity, Long id);

    protected abstract String getUsername(E entity);
}
