package orlov.programming.spring_core_gym.dao;

import java.util.List;

public interface DAO<T> {
    T create(T t);

    T update(T t);

    void delete(T t);

    List<T> findAll();

    T findByObject(T t);
}
