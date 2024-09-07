package orlov.programming.spring_core_gym.dao.impl.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.storage.Storage;

import java.util.List;
import java.util.Map;

@Repository
public class TrainingDAO implements DAO<Training> {
    private static final String NOT_FOUND_ERROR_MESSAGE = "Training is not found for ";

    private final Map<Long, Training> trainingHashMap;
    private static long nextId;

    @Autowired
    public TrainingDAO(Storage<Long, Training> storage) {
        this.trainingHashMap = storage.getStorage();
        nextId = storage.getLastKey();
    }

    @Override
    public Training create(Training training) {
        return trainingHashMap.put(nextId, training);
    }

    @Override
    public Training update(Training training) {
        Long key = getKeyByValue(training);

        return trainingHashMap.put(key, training);
    }

    @Override
    public void delete(Training training) {
        Long key = getKeyByValue(training);

        trainingHashMap.remove(key);
    }

    @Override
    public List<Training> findAll() {
        return trainingHashMap.values().stream().toList();
    }

    @Override
    public Training findByObject(Training training) {
        return trainingHashMap.get(getKeyByValue(training));
    }

    public Long getKeyByValue(Training training) {
        for (Map.Entry<Long, Training> entry : trainingHashMap.entrySet()) {
            if (entry.getValue().equals(training)) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE + training);
    }
}