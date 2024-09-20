package orlov.programming.springcoregym.service;

public interface UpdatableService<T> extends CSService<T>{
    T update(T t);
}
