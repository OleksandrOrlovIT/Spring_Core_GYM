package orlov.programming.springcoregym.storage;

import java.util.Map;

public interface Storage {

    <E> Map<Long, E> getStorage(Class<E> entityClass);

    <E> Long getNextId(Class<E> entityClass);

    void populateStorage();
}
