package orlov.programming.springcoregym.service;

public interface UpdatableService<E, ID> extends CSService<E, ID>{
    E update(E e);
}
