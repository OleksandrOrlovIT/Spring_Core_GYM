package orlov.programming.springcoregym.dao.impl.training;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Repository
public class TrainingDaoImpl implements TrainingDao {
    private static final String TRAINING_NULL_ERROR = "Training can't be null";
    private static final String TRAINING_NOT_FOUND_ERROR_MESSAGE = "Training is not found for ";
    private static final String TRAINEE_NOT_FOUND_ERROR_MESSAGE = "Trainee is not found for ";
    private static final String TRAINER_NOT_FOUND_ERROR_MESSAGE = "Trainer is not found for ";

    private final TraineeDao traineeDAO;
    private final TrainerDao trainerDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TrainingDaoImpl(EntityManager entityManager, TraineeDao traineeDAO, TrainerDao trainerDAO) {
        this.entityManager = entityManager;
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Training create(Training training) {
        Objects.requireNonNull(training, TRAINING_NULL_ERROR);
        isTraineeAndTrainerExists(training.getTrainee(), training.getTrainer());

        log.info("Created new Training = {}", training);

        return null;
    }

    @Override
    public List<Training> findAll() {
        return null;
    }

    @Override
    public Optional<Training> findByObject(Training training) {
        Objects.requireNonNull(training, TRAINING_NULL_ERROR);
        return Optional.empty();
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
