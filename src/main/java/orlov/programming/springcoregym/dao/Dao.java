package orlov.programming.springcoregym.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E, ID> {
    E create(E e);

    E update(E e);

    void deleteById(ID id);

    List<E> findAll();

    Optional<E> findById(ID id);
}
