package orlov.programming.springcoregym.service;

import java.util.List;

public interface CSService <E, ID>{
    E create(E e);
    E select(ID id);
    List<E> findAll();
}
