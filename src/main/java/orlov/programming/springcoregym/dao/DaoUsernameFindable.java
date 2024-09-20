package orlov.programming.springcoregym.dao;

import java.util.Optional;

public interface DaoUsernameFindable<E> extends Dao<E> {
    Optional<E> findByUsername(String username);
}
