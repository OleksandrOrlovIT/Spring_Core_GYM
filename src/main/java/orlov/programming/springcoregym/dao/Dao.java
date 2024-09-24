package orlov.programming.springcoregym.dao;

import java.util.List;

public interface Dao<E, ID> {
    E create(E e);

    E update(E e);

    void deleteById(ID id);

    List<E> findAll();

    E findById(ID id);
}
