package orlov.programming.springcoregym.dao;

import java.util.Optional;

public interface DaoUsernameFindable<E, ID> extends Dao<E, ID> {
    Optional<E> getByUsername(String username);
}
