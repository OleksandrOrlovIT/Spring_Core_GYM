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
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.util.model.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
@Sql(scripts = "/sql/trainer/populate_trainer.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class TrainerDaoImplTest {

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainingDao trainingDao;

    private Trainer testTrainer;
    private TrainingType testTrainingType;

    private static final String USERNAME = "testUser";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "pass";
    private static final String SPECIALIZATION = "specialization";
    private static final boolean IS_ACTIVE = true;

    @BeforeEach
    void setUp() {
        assertEquals(1, trainingTypeDao.getAll().size());
        assertEquals(2, trainerDao.getAll().size());
        assertEquals(1, traineeDao.getAll().size());
        assertEquals(2, trainingDao.getAll().size());

        testTrainingType = trainingTypeDao.getAll().get(0);

        testTrainer = Trainer.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .specialization(testTrainingType)
                .isActive(IS_ACTIVE)
                .build();
    }

    @Test
    @Transactional
    void createTrainer() {
        testTrainer = trainerDao.create(testTrainer);

        assertNotNull(testTrainer);
        assertNotNull(testTrainer.getId());
    }

    @Test
    void getByIdTrainer() {
        testTrainer = trainerDao.getAll().get(0);
        Optional<Trainer> foundTrainer = trainerDao.getById(testTrainer.getId());

        assertTrue(foundTrainer.isPresent());
        assertEquals(testTrainer, foundTrainer.get());
    }

    @Test
    void getByUsernameTrainer() {
        Trainer trainer = trainerDao.getAll().get(0);

        Optional<Trainer> found = trainerDao.getByUsername(trainer.getUsername());
        assertTrue(found.isPresent());
        assertEquals(trainer, found.get());
    }

    @Test
    @Transactional
    void deleteTrainer() {
        Trainer trainer = trainerDao.getAll().get(0);

        trainerDao.deleteById(trainer.getId());
        Optional<Trainer> deleted = trainerDao.getById(trainer.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void deleteNonExistentTrainer() {
        assertDoesNotThrow(() -> trainerDao.deleteById(-1L));
    }

    @Test
    @Transactional
    void updateTrainer() {
        Trainer savedTrainer = trainerDao.getAll().get(0);

        String delim = "1";

        TrainingType diffTrainingType = TrainingType
                .builder()
                .trainingTypeName(SPECIALIZATION + delim)
                .build();

        diffTrainingType = trainingTypeDao.create(diffTrainingType);

        Trainer diffTrainer = Trainer.builder()
                .username(savedTrainer.getUsername() + delim)
                .firstName(savedTrainer.getFirstName() + delim)
                .lastName(savedTrainer.getLastName() + delim)
                .password(savedTrainer.getPassword() + delim)
                .isActive(!savedTrainer.getIsActive())
                .specialization(diffTrainingType)
                .build();

        Trainer updated = trainerDao.update(diffTrainer);

        assertNotEquals(savedTrainer, updated);
        assertEquals(savedTrainer.getUsername() + delim, updated.getUsername());
        assertEquals(savedTrainer.getFirstName() + delim, updated.getFirstName());
        assertEquals(savedTrainer.getLastName() + delim, updated.getLastName());
        assertEquals(savedTrainer.getPassword() + delim, updated.getPassword());
        assertEquals(!savedTrainer.getIsActive(), updated.getIsActive());
        assertEquals(diffTrainingType.getTrainingTypeName(),
                updated.getSpecialization().getTrainingTypeName());
    }

    @Test
    void getAllTrainers() {
        List<Trainer> trainerList = trainerDao.getAll();

        assertNotNull(trainerList);
        assertEquals(2, trainerList.size());
    }

    @Test
    void getByUsernameThenException() {
        Optional<Trainer> optionalTrainer = trainerDao.getByUsername("");

        assertTrue(optionalTrainer.isEmpty());
    }

    @Test
    void getTrainingsByDateThenReturnTrainingsAndUsername() {
        testTrainingType = trainingTypeDao.getAll().get(0);
        testTrainer = trainerDao.getAll().get(0);
        LocalDate date = LocalDate.of(2024, 10, 1);

        List<Training> foundTrainings =
                trainerDao.getTrainingsByDateAndUsername(date, date.plusDays(2), testTrainer.getUsername());

        assertNotNull(foundTrainings);
        assertEquals(2, foundTrainings.size());
    }

    @Test
    void getTrainersWithoutPassedTraineeGiven2TrainerWithoutTraineesThenSuccess() {
        testTrainer = trainerDao.getAll().get(0);
        Trainer testTrainer2 = trainerDao.getAll().get(1);
        Trainee testTrainee = traineeDao.getAll().get(0);

        List<Trainer> trainers = trainerDao.getTrainersWithoutPassedTrainee(testTrainee, new Pageable(0, 2));

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(testTrainer));
        assertTrue(trainers.contains(testTrainer2));
    }

    @Test
    void getByIdsGiven2TrainersThenSuccess() {
        List<Trainer> trainers = trainerDao.getAll();

        List<Long> ids = List.of(trainers.get(0).getId(), trainers.get(1).getId());

        List<Trainer> foundTrainers = trainerDao.getByIds(ids);

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertEquals(trainers, foundTrainers);
    }

    @Test
    void getByIdsGivenNullIdsThenException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> trainerDao.getByIds(null));
        assertEquals("Ids can't be null", e.getMessage());
    }

    @Test
    void getByIdsGivenEmptyIdsThenEmptyList() {
        List<Trainer> trainers = trainerDao.getByIds(List.of());

        assertNotNull(trainers);
        assertEquals(0, trainers.size());
    }
}
