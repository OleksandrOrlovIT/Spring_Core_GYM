package orlov.programming.spring_core_gym.service;

public interface CRUDService<T> extends UpdatableService<T>{
    void delete(T t);
}
