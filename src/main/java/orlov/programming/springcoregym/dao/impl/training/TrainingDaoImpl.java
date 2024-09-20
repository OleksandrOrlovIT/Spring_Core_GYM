package orlov.programming.springcoregym.dao.impl.training;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Repository
public class TrainingDaoImpl implements TrainingDao {
    private static final String TRAINING_NULL_ERROR = "Training can't be null";
    private static final String TRAINING_NOT_FOUND_ERROR_MESSAGE = "Training is not found for ";
    private static final String TRAINEE_NOT_FOUND_ERROR_MESSAGE = "Trainee is not found for ";
    private static final String TRAINER_NOT_FOUND_ERROR_MESSAGE = "Trainer is not found for ";

    private final Map<Long, Training> trainingHashMap;
    private long nextId;

    private final TraineeDao traineeDAO;
    private final TrainerDao trainerDAO;

    @Autowired
    public TrainingDaoImpl(Storage storage, TraineeDao traineeDAO, TrainerDao trainerDAO) {
        this.trainingHashMap = storage.getStorage(Training.class);
        nextId = storage.getNextId(Training.class);
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
    public Optional<Training> findByObject(Training training) {
        Objects.requireNonNull(training, TRAINING_NULL_ERROR);
        return Optional.ofNullable(trainingHashMap.get(getKeyByValue(training)));
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
        if(traineeDAO.findByObject(trainee).isEmpty()){
            throw new IllegalArgumentException(TRAINEE_NOT_FOUND_ERROR_MESSAGE + trainee);
        }

        if(trainerDAO.findByObject(trainer).isEmpty()){
            throw new IllegalArgumentException(TRAINER_NOT_FOUND_ERROR_MESSAGE + trainer);
        }
    }
}
