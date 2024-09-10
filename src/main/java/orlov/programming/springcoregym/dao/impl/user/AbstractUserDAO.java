package orlov.programming.springcoregym.dao.impl.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Repository
public abstract class AbstractUserDAO<E> implements DAOUsernameFindable<E> {
    
    protected final Map<Long, E> userHashMap;
    protected final Class<E> entityClass;
    protected long nextId;

    @Autowired
    public AbstractUserDAO(Storage storage, Class<E> entityClass) {
        this.entityClass = entityClass;
        this.userHashMap = storage.getStorage(entityClass);
        this.nextId = storage.getNextId(entityClass);
    }

    protected abstract String getNullEntityErrorMessage();
    
    protected abstract void checkEntityIdIsNull(Long id);
    
    @Override
    public E create(E entity) {
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        if (getUserId(entity) != null) {
            throw new IllegalArgumentException("Entity's id has to be null");
        }

        setUserId(entity, nextId);
        nextId++;

        userHashMap.put(getUserId(entity), entity);
        log.info("Created new {} = {}", getClass().getSimpleName(), entity);

        return entity;
    }

    @Override
    public E update(E entity) {
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        checkEntityIdIsNull(getUserId(entity));

        if (findByObject(entity).isEmpty()) {
            IllegalArgumentException e = new IllegalArgumentException("Entity does not exist");
            log.error(e);
            throw e;
        }

        userHashMap.put(getUserId(entity), entity);
        log.info("Updating {} = {}", getClass().getSimpleName(), entity);
        return entity;
    }

    @Override
    public void delete(E entity) {
        log.info("Deleting {} = {}", getClass().getSimpleName(), entity);
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        checkEntityIdIsNull(getUserId(entity));

        userHashMap.remove(getUserId(entity));
    }

    @Override
    public List<E> findAll() {
        return userHashMap.values().stream().toList();
    }

    @Override
    public Optional<E> findByObject(E entity) {
        Objects.requireNonNull(entity, getNullEntityErrorMessage());
        checkEntityIdIsNull(getUserId(entity));

        return Optional.ofNullable(userHashMap.get(getUserId(entity)));
    }

    @Override
    public Optional<E> findByUsername(String username) {
        Objects.requireNonNull(username, "Username can't be null");
        for (E user : userHashMap.values()) {
            if (user != null && username.equals(getUsername(user))) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    protected abstract Long getUserId(E entity);

    protected abstract void setUserId(E entity, Long id);

    protected abstract String getUsername(E entity);
}