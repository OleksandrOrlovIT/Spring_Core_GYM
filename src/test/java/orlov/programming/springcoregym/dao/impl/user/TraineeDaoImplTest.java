package orlov.programming.springcoregym.dao.impl.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.dao.impl.TestDaoConfig;
import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
import orlov.programming.springcoregym.dao.impl.training.TrainingTypeDao;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.dto.trainee.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
@Sql(scripts = "/sql/trainee/populate_trainee.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class TraineeDaoImplTest {

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingDao trainingDao;

    private Trainee testTrainee;

    private static final String USERNAME = "testUser";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "pass";
    private static final boolean IS_ACTIVE = true;
    private static final LocalDate TRAINING_DATE = LocalDate.of(2024, 10, 1);

    @BeforeEach
    void setUp() {
        testTrainee = Trainee.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();

        assertEquals(1, traineeDao.getAll().size());
        assertEquals(1, trainingTypeDao.getAll().size());
        assertEquals(1, trainerDao.getAll().size());
        assertEquals(2, trainingDao.getAll().size());
    }

    @Test
    @Transactional
    void createTrainee() {
        testTrainee = traineeDao.create(testTrainee);

        assertNotNull(testTrainee);
        assertNotNull(testTrainee.getId());
    }

    @Test
    void getByIdTrainee() {
        testTrainee = traineeDao.getAll().get(0);
        Optional<Trainee> foundTrainee = traineeDao.getById(testTrainee.getId());

        assertTrue(foundTrainee.isPresent());
        assertEquals(testTrainee, foundTrainee.get());
    }

    @Test
    void getByUsernameTrainee() {
        Trainee trainee = traineeDao.getAll().get(0);

        Optional<Trainee> found = traineeDao.getByUsername(trainee.getUsername());
        assertTrue(found.isPresent());
        assertEquals(trainee, found.get());
    }

    @Test
    @Transactional
    void deleteTrainee() {
        Trainee trainee = traineeDao.getAll().get(0);

        traineeDao.deleteById(trainee.getId());
        Optional<Trainee> deleted = traineeDao.getById(trainee.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void deleteNonExistentTrainee() {
        assertDoesNotThrow(() -> traineeDao.deleteById(-1L));
    }

    @Test
    @Transactional
    void updateTrainee() {
        Trainee savedTrainee = traineeDao.getAll().get(0);

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
        List<Trainee> traineeList = traineeDao.getAll();

        assertNotNull(traineeList);
        assertEquals(1, traineeList.size());
    }

    @Test
    void getByUsernameGivenNothingThenException() {
        Optional<Trainee> optionalTrainee = traineeDao.getByUsername("");

        assertTrue(optionalTrainee.isEmpty());
    }

    @Test
    void getTrainingsByDateThenReturnTrainingsAndUsername() {
        TrainingType testTrainingType = trainingTypeDao.getAll().get(0);
        testTrainee = traineeDao.getAll().get(0);

        TraineeTrainingDTO traineeTrainingDTO = new TraineeTrainingDTO(TRAINING_DATE,
                TRAINING_DATE.plusDays(2),
                testTrainee.getUsername(), testTrainingType.getTrainingTypeName());
        List<Training> foundTrainings = traineeDao.getTrainingsByTraineeTrainingDTO(traineeTrainingDTO);

        assertNotNull(foundTrainings);
        assertEquals(2, foundTrainings.size());
    }

    //New trainee + old trainee from the database
    @Test
    @Transactional
    void deleteByUsernameThenSuccess() {
        Trainee savedTrainee = traineeDao.create(testTrainee);

        assertEquals(2, traineeDao.getAll().size());

        traineeDao.deleteByUsername(savedTrainee.getUsername());

        assertEquals(1, traineeDao.getAll().size());
    }

    @Test
    void deleteByUsernameThenNothing() {
        assertDoesNotThrow(() -> traineeDao.deleteByUsername(USERNAME));

        assertTrue(traineeDao.getByUsername(USERNAME).isEmpty());
    }
}
