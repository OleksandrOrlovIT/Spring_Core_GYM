package orlov.programming.springcoregym.dao.impl.user;

import orlov.programming.springcoregym.dao.DAO;

import java.util.Optional;

public interface DAOUsernameFindable<E> extends DAO<E> {
    Optional<E> findByUsername(String username);
}
