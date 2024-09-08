package orlov.programming.spring_core_gym.service;

public interface UpdatableService<T> extends CSService<T>{
    T update(T t);
}
