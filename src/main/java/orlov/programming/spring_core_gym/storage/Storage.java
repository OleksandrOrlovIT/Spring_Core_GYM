package orlov.programming.spring_core_gym.storage;

import java.util.Map;

public interface Storage<K, V> {
    Map<K, V> getStorage();

    void populateStorage();

    K getLastKey();
}