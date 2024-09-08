package orlov.programming.spring_core_gym.dao;

import java.util.List;

public interface DAOSelectableCreatable <T>{
    T create(T t);

    List<T> findAll();

    T findByObject(T t);
}
