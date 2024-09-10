package orlov.programming.springcoregym.dao;

public interface DAO<E> extends DAOSelectableCreatable<E> {
    E update(E e);

    void delete(E e);
}
