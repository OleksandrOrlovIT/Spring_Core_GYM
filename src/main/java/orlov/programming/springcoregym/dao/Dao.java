package orlov.programming.springcoregym.dao;

public interface Dao<E> extends DaoSelectableCreatable<E> {
    E update(E e);

    void delete(E e);
}
