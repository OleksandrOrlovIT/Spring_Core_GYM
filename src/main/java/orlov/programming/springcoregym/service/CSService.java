package orlov.programming.springcoregym.service;

public interface CSService <T>{
    T create(T t);
    T select(T t);
}
