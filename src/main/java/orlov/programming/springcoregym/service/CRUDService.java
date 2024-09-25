package orlov.programming.springcoregym.service;

public interface CRUDService<E, ID> extends UpdatableService<E, ID>{
    void deleteByUsername(String userName);
}
