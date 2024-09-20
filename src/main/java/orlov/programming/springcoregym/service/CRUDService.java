package orlov.programming.springcoregym.service;

public interface CRUDService<T> extends UpdatableService<T>{
    void delete(T t);
}
