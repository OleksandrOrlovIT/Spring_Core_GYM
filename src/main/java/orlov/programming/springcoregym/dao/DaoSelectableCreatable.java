package orlov.programming.springcoregym.dao;

import java.util.List;
import java.util.Optional;

public interface DaoSelectableCreatable<E>{
    E create(E e);

    List<E> findAll();

    Optional<E> findByObject(E e);
}
