package orlov.programming.springcoregym.service;

public interface CSService <E, ID>{
    E create(E e);
    E select(ID id);
}
