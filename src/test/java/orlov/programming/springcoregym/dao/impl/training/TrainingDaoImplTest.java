package orlov.programming.springcoregym.dao.impl.training;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.dao.impl.TestDaoConfig;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
class TrainingDaoImplTest {

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    private Training testTraining;
    private TrainingType testTrainingType;
    private Trainer testTrainer;
    private Trainee testTrainee;

    private static final String TRAINING_NAME = "testTrainingName";

    @BeforeEach
    void setUpEntities() {
        testTrainingType = TrainingType.builder()
                .trainingTypeName("testTrainingType1")
                .build();
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainee = Trainee.builder()
                .username("testTrainee")
                .firstName("First1")
                .lastName("Last1")
                .password("pass1")
                .isActive(true)
                .build();
        testTrainee = traineeDao.create(testTrainee);

        testTrainer = Trainer.builder()
                .username("testTrainer")
                .firstName("First1")
                .lastName("Last1")
                .password("pass1")
                .isActive(true)
                .specialization(testTrainingType)
                .build();
        testTrainer = trainerDao.create(testTrainer);

        testTraining = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName(TRAINING_NAME)
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();
    }

    @Test
    void createTraining() {
        testTraining = trainingDao.create(testTraining);

        assertNotNull(testTraining);
        assertNotNull(testTraining.getId());
    }

    @Test
    void getByIdTraining() {
        testTraining = trainingDao.create(testTraining);
        Optional<Training> foundTraining = trainingDao.getById(testTraining.getId());

        assertTrue(foundTraining.isPresent());
        assertEquals(testTraining, foundTraining.get());
    }

    @Test
    void deleteTraining() {
        testTraining = trainingDao.create(testTraining);

        trainingDao.deleteById(testTraining.getId());
        Optional<Training> deleted = trainingDao.getById(testTraining.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void deleteNonExistentTraining() {
        assertDoesNotThrow(() -> trainingDao.deleteById(-1L));
    }

    @Test
    void updateTraining() {
        Training savedTraining = trainingDao.create(testTraining);

        String delim = "1";

        TrainingType testTrainingTypeForUpdate = TrainingType.builder()
                .trainingTypeName(testTrainingType.getTrainingTypeName() + delim)
                .build();

        Trainer testTrainerForUpdate = Trainer.builder()
                .username(testTrainer.getUsername() + delim)
                .firstName(testTrainer.getFirstName() + delim)
                .lastName(testTrainer.getLastName() + delim)
                .password(testTrainer.getPassword() + delim)
                .isActive(!testTrainer.getIsActive())
                .specialization(testTrainingType)
                .build();

        Trainee testTraineeForUpdate = Trainee.builder()
                .username(testTrainee.getUsername() + delim)
                .firstName(testTrainee.getFirstName() + delim)
                .lastName(testTrainee.getLastName() + delim)
                .password(testTrainee.getPassword() + delim)
                .isActive(!testTrainee.getIsActive())
                .build();

        testTrainingType = trainingTypeDao.create(testTrainingTypeForUpdate);
        testTrainer = trainerDao.create(testTrainerForUpdate);
        testTrainee = traineeDao.create(testTraineeForUpdate);

        Training diffTraining = Training.builder()
                .id(savedTraining.getId())
                .trainee(testTraineeForUpdate)
                .trainer(testTrainerForUpdate)
                .trainingName(TRAINING_NAME + delim)
                .trainingType(testTrainingTypeForUpdate)
                .trainingDate(LocalDate.MIN.plusDays(1))
                .trainingDuration(10L)
                .build();

        Training updated = trainingDao.update(diffTraining);

        assertEquals(testTraineeForUpdate, updated.getTrainee());
        assertEquals(testTrainerForUpdate, updated.getTrainer());
        assertEquals(testTrainingTypeForUpdate, updated.getTrainingType());
        assertEquals(TRAINING_NAME + delim, updated.getTrainingName());
        assertEquals(LocalDate.MIN.plusDays(1), updated.getTrainingDate());
        assertEquals(10L, updated.getTrainingDuration());
    }

    @Test
    void getAllTrainings() {
        Training training2 = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName(TRAINING_NAME)
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN.plusDays(1))
                .trainingDuration(10L)
                .build();

        trainingDao.create(testTraining);
        trainingDao.create(training2);

        List<Training> trainingList = trainingDao.getAll();

        assertNotNull(trainingList);
        assertEquals(2, trainingList.size());
    }

    @AfterEach
    public void setAfter() {
        for (Training training : trainingDao.getAll()) {
            trainingDao.deleteById(training.getId());
        }

        for (Trainer trainer : trainerDao.getAll()) {
            trainerDao.deleteById(trainer.getId());
        }

        for (Trainee trainee : traineeDao.getAll()) {
            traineeDao.deleteById(trainee.getId());
        }

        for (TrainingType trainingType : trainingTypeDao.getAll()) {
            trainingTypeDao.deleteById(trainingType.getId());
        }
    }
}
