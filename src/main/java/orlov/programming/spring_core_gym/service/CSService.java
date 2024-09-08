package orlov.programming.spring_core_gym.service;

public interface CSService <T>{
    T create(T t);
    T select(T t);
}
