package orlov.programming.spring_core_gym.dao.impl.training;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.dao.DAOSelectableCreatable;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Repository
public class TrainingDAO implements DAOSelectableCreatable<Training> {
    private static final String TRAINING_NULL_ERROR = "Training can't be null";
    private static final String TRAINING_NOT_FOUND_ERROR_MESSAGE = "Training is not found for ";
    private static final String TRAINEE_NOT_FOUND_ERROR_MESSAGE = "Trainee is not found for ";
    private static final String TRAINER_NOT_FOUND_ERROR_MESSAGE = "Trainer is not found for ";

    private final Map<Long, Training> trainingHashMap;
    private long nextId;

    private final DAO<Trainee> traineeDAO;
    private final DAO<Trainer> trainerDAO;

    @Autowired
    public TrainingDAO(Storage<Long, Training> storage, DAO<Trainee> traineeDAO, DAO<Trainer> trainerDAO) {
        log.info("Creating TrainingDAO");
        this.trainingHashMap = storage.getStorage();
        nextId = storage.getLastKey();
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Training create(Training training) {
        Objects.requireNonNull(training, TRAINING_NULL_ERROR);
        isTraineeAndTrainerExists(training.getTrainee(), training.getTrainer());

        trainingHashMap.put(nextId, training);
        nextId++;

        log.info("Created new Training = {}", training);

        return training;
    }

    @Override
    public List<Training> findAll() {
        return trainingHashMap.values().stream().toList();
    }

    @Override
    public Training findByObject(Training training) {
        Objects.requireNonNull(training, TRAINING_NULL_ERROR);
        return trainingHashMap.get(getKeyByValue(training));
    }

    public Long getKeyByValue(Training training) {
        Objects.requireNonNull(training, TRAINING_NULL_ERROR);

        for (Map.Entry<Long, Training> entry : trainingHashMap.entrySet()) {
            Training curr = entry.getValue();
            if (training.equals(curr)) {
                return entry.getKey();
            }
        }

        IllegalArgumentException e = new IllegalArgumentException(TRAINING_NOT_FOUND_ERROR_MESSAGE + training);
        log.error(e);
        throw e;
    }

    public void isTraineeAndTrainerExists(Trainee trainee, Trainer trainer){
        if(traineeDAO.findByObject(trainee) == null){
            throw new IllegalArgumentException(TRAINEE_NOT_FOUND_ERROR_MESSAGE + trainee);
        }

        if(trainerDAO.findByObject(trainer) == null){
            throw new IllegalArgumentException(TRAINER_NOT_FOUND_ERROR_MESSAGE + trainer);
        }
    }
}