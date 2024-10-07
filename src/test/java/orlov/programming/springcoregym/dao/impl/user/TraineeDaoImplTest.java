package orlov.programming.springcoregym.dao.impl.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.dao.impl.TestDaoConfig;
import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
import orlov.programming.springcoregym.dao.impl.training.TrainingTypeDao;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
class TraineeDaoImplTest {

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainingDao trainingDao;

    private Trainee testTrainee;

    private static final String USERNAME = "testUser";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "pass";
    private static final boolean IS_ACTIVE = true;

    @BeforeEach
    void setUp() {
        testTrainee = Trainee.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }

    @Test
    void createTrainee() {
        testTrainee = traineeDao.create(testTrainee);

        assertNotNull(testTrainee);
        assertNotNull(testTrainee.getId());
    }

    @Test
    void getByIdTrainee() {
        testTrainee = traineeDao.create(testTrainee);
        Optional<Trainee> foundTrainee = traineeDao.getById(testTrainee.getId());

        assertTrue(foundTrainee.isPresent());
        assertEquals(testTrainee, foundTrainee.get());
    }

    @Test
    void getByUsernameTrainee() {
        Trainee trainee = traineeDao.create(testTrainee);

        Optional<Trainee> found = traineeDao.getByUsername(trainee.getUsername());
        assertTrue(found.isPresent());
        assertEquals(trainee, found.get());
    }

    @Test
    void deleteTrainee() {
        Trainee trainee = traineeDao.create(testTrainee);

        traineeDao.deleteById(trainee.getId());
        Optional<Trainee> deleted = traineeDao.getById(trainee.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void deleteNonExistentTrainee() {
        assertDoesNotThrow(() -> traineeDao.deleteById(-1L));
    }

    @Test
    void updateTrainee() {
        Trainee savedTrainee = traineeDao.create(testTrainee);

        String delim = "1";

        Trainee diffTrainee = Trainee.builder()
                .username(savedTrainee.getUsername() + delim)
                .firstName(savedTrainee.getFirstName() + delim)
                .lastName(savedTrainee.getLastName() + delim)
                .password(savedTrainee.getPassword() + delim)
                .isActive(!savedTrainee.getIsActive())
                .build();

        Trainee updated = traineeDao.update(diffTrainee);

        assertNotEquals(updated, savedTrainee);
        assertEquals(updated.getUsername(), savedTrainee.getUsername() + delim);
        assertEquals(updated.getFirstName(), savedTrainee.getFirstName() + delim);
        assertEquals(updated.getLastName(), savedTrainee.getLastName() + delim);
        assertEquals(updated.getPassword(), savedTrainee.getPassword() + delim);
        assertEquals(updated.getIsActive(), !savedTrainee.getIsActive());
    }

    @Test
    void getAllTrainees() {
        Trainee trainee1 = Trainee.builder()
                .username(USERNAME + 1)
                .firstName(FIRST_NAME + 1)
                .lastName(LAST_NAME + 1)
                .password(PASSWORD + 1)
                .isActive(IS_ACTIVE)
                .build();

        Trainee trainee2 = Trainee.builder()
                .username(USERNAME + 2)
                .firstName(FIRST_NAME + 2)
                .lastName(LAST_NAME + 2)
                .password(PASSWORD + 2)
                .isActive(IS_ACTIVE)
                .build();

        traineeDao.create(trainee1);
        traineeDao.create(trainee2);

        List<Trainee> traineeList = traineeDao.getAll();

        assertNotNull(traineeList);
        assertEquals(2, traineeList.size());
    }

    @Test
    void getByUsernameGivenNothingThenException() {
        Optional<Trainee> optionalTrainee = traineeDao.getByUsername("");

        assertTrue(optionalTrainee.isEmpty());
    }

    @Test
    void getTrainingsByDateThenReturnTrainingsAndUsername() {
        TrainingType testTrainingType = TrainingType.builder()
                .trainingTypeName("testTrainingType1")
                .build();

        Trainer testTrainer = Trainer.builder()
                .username(USERNAME + "TRAINER")
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .specialization(testTrainingType)
                .isActive(IS_ACTIVE)
                .build();

        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer = trainerDao.create(testTrainer);
        testTrainee = traineeDao.create(testTrainee);

        Training testTraining1 = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName("TRAINING_NAME")
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();

        Training testTraining2 = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName("TRAINING_NAME")
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN.plusDays(1))
                .trainingDuration(10L)
                .build();

        trainingDao.create(testTraining1);
        trainingDao.create(testTraining2);

        TraineeTrainingDTO traineeTrainingDTO = new TraineeTrainingDTO(LocalDate.MIN, LocalDate.MIN.plusDays(1),
                testTrainee.getUsername(), testTrainingType.getTrainingTypeName());
        List<Training> foundTrainings = traineeDao.getTrainingsByTraineeTrainingDTO(traineeTrainingDTO);

        assertNotNull(foundTrainings);
        assertEquals(2, foundTrainings.size());
    }

    @Test
    void deleteByUsernameThenSuccess() {
        Trainee savedTrainee = traineeDao.create(testTrainee);

        assertEquals(1, traineeDao.getAll().size());

        traineeDao.deleteByUsername(savedTrainee.getUsername());

        assertEquals(0, traineeDao.getAll().size());
    }

    @Test
    void deleteByUsernameThenNothing() {
        assertDoesNotThrow(() -> traineeDao.deleteByUsername(USERNAME));

        assertEquals(0, traineeDao.getAll().size());
    }

    @AfterEach
    public void setAfter() {
        for (Trainee trainee : traineeDao.getAll()) {
            traineeDao.deleteById(trainee.getId());
        }
    }
}
