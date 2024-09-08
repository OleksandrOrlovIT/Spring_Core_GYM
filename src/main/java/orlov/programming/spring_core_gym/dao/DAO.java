package orlov.programming.spring_core_gym.dao;

public interface DAO<T> extends DAOSelectableCreatable<T>{
    T update(T t);

    void delete(T t);
}
