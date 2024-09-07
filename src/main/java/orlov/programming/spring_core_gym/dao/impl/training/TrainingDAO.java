package orlov.programming.spring_core_gym.dao.impl.training;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.storage.Storage;

import java.util.List;
import java.util.Map;

@Log4j2
@Repository
public class TrainingDAO implements DAO<Training> {
    private static final String NOT_FOUND_ERROR_MESSAGE = "Training is not found for ";

    private final Map<Long, Training> trainingHashMap;
    private static long nextId;

    @Autowired
    public TrainingDAO(Storage<Long, Training> storage) {
        log.info("Creating TrainingDAO");
        this.trainingHashMap = storage.getStorage();
        nextId = storage.getLastKey();
    }

    @Override
    public Training create(Training training) {
        Training savedTraining = trainingHashMap.put(nextId, training);

        log.info("Creating new Training = {}", savedTraining);

        return savedTraining;
    }

    @Override
    public Training update(Training training) {
        Long key = getKeyByValue(training);

        Training updatedTraining = trainingHashMap.put(key, training);

        log.info("Updating Training = {}", updatedTraining);

        return updatedTraining;
    }

    @Override
    public void delete(Training training) {
        log.info("Deleting Training = {}", training);

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

        IllegalArgumentException e = new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE + training);
        log.error(e);
        throw e;
    }
}