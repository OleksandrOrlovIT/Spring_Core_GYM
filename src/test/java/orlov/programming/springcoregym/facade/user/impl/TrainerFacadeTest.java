package orlov.programming.springcoregym.facade.user.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.TestConfig;
import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
import orlov.programming.springcoregym.dao.impl.training.TrainingTypeDao;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.facade.user.TrainerFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.util.model.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@Sql(scripts = "/sql/trainer/populate_trainer.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class TrainerFacadeTest {

    @Autowired
    private TrainerFacade facade;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private TraineeDao traineeDao;

    private Training testTraining;
    private TrainingType testTrainingType;
    private Trainer testTrainer;
    private Trainee testTrainee;

    @BeforeEach
    void setFacade() {
        assertEquals(1, trainingTypeDao.getAll().size());
        assertEquals(2, trainerDao.getAll().size());
        assertEquals(1, traineeDao.getAll().size());
        assertEquals(2, trainingDao.getAll().size());

        testTrainingType = trainingTypeDao.getAll().get(0);
        testTrainee = traineeDao.getAll().get(0);
        testTrainer = trainerDao.getAll().get(0);
        testTraining = trainingDao.getAll().get(0);
    }

    @Test
    void createTrainerGivenNullThenEmpty() {
        Optional<Trainer> savedTrainer = facade.createTrainer(null);
        assertTrue(savedTrainer.isEmpty());
    }

    @Test
    void isTrainerUsernamePasswordMatchGivenNullThenFalse() {
        boolean res = facade.isTrainerUsernamePasswordMatch(null, null);

        assertFalse(res);
    }

    @Test
    void isTrainerUsernamePasswordMatchGivenValidThenTrue() {
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);
        boolean res = facade.isTrainerUsernamePasswordMatch(testTrainer.getUsername(), testTrainer.getPassword());

        assertTrue(res);
    }

    @Test
    void updateTrainerGivenNullThenEmpty() {
        Optional<Trainer> trainer = facade.updateTrainer(null);

        assertTrue(trainer.isEmpty());
    }

    @Test
    void selectTrainerGivenNullThenEmpty() {
        Optional<Trainer> trainer = facade.selectTrainer(null);

        assertTrue(trainer.isEmpty());
    }

    @Test
    void activateTrainerGivenNotFoundThenEmpty() {
        Optional<Trainer> trainer = facade.activateTrainer(testTrainer.getUsername());

        assertTrue(trainer.isEmpty());
    }

    @Test
    @Transactional
    void activateTrainerGivenValidThenSuccess() {
        testTrainer.setIsActive(false);
        testTrainer = trainerDao.update(testTrainer);
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        Optional<Trainer> trainer = facade.activateTrainer(testTrainer.getUsername());

        assertTrue(trainer.isPresent());
        assertTrue(trainer.get().getIsActive());
    }

    @Test
    void changeTrainerPasswordGivenNullThenEmpty() {
        Optional<Trainer> savedTrainer = facade.changeTrainerPassword(null, null);

        assertTrue(savedTrainer.isEmpty());
    }

    @Test
    @Transactional
    void changeTrainerPasswordGivenValidThenSuccess() {
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        String newPassword = "newPassword";

        Optional<Trainer> savedTrainer = facade.changeTrainerPassword(testTrainer.getUsername(), newPassword);

        assertTrue(savedTrainer.isPresent());
        assertEquals(newPassword, savedTrainer.get().getPassword());
    }

    @Test
    void getTrainingsByDateTrainerNameGivenValidThenSuccess() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        List<Training> trainings = facade.getTrainingsByDateTrainerName(
                testTraining.getTrainingDate(), testTraining.getTrainingDate().plusDays(1), testTrainer.getUsername());

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        assertEquals(trainings, trainingDao.getAll());
    }

    @Test
    @Transactional
    void getTrainersWithoutPassedTraineeGivenValidThenSuccess() {
        Trainee newTrainee = Trainee.builder()
                .firstName(testTrainee.getFirstName())
                .lastName(testTrainee.getLastName())
                .password(testTrainee.getPassword())
                .username(testTrainee.getUsername() + "1")
                .isActive(testTrainer.getIsActive())
                .build();
        newTrainee = traineeDao.create(newTrainee);

        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        List<Trainer> trainers =
                facade.getTrainersWithoutPassedTrainee(newTrainee.getUsername(), new Pageable(0, 2));

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertEquals(trainers, trainerDao.getAll());
    }

    @Test
    void createTrainer() {
        Trainer trainer = Trainer.builder()
                .firstName(testTrainer.getFirstName())
                .lastName(testTrainer.getLastName())
                .password(testTrainer.getPassword())
                .isActive(testTrainer.getIsActive())
                .specialization(testTrainingType)
                .build();
        Optional<Trainer> savedTrainer = facade.createTrainer(trainer);

        assertTrue(savedTrainer.isPresent());
        assertEquals(trainer.getFirstName(), savedTrainer.get().getFirstName());
        assertEquals(trainer.getLastName(), savedTrainer.get().getLastName());
    }

    @Test
    @Transactional
    void updateTrainer() {
        String addition = "1";
        TrainingType updatedTrainingType = TrainingType.builder()
                .trainingTypeName("newSpecialization")
                .build();

        updatedTrainingType = trainingTypeDao.create(updatedTrainingType);
        testTrainer.setSpecialization(updatedTrainingType);

        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);
        Optional<Trainer> updatedTrainerOptional = facade.updateTrainer(testTrainer);
        assertTrue(updatedTrainerOptional.isPresent());
        Trainer updatedTrainer = updatedTrainerOptional.get();

        assertNotNull(updatedTrainer);
        assertEquals(updatedTrainer.getSpecialization(), updatedTrainingType);
    }

    @Test
    void selectTrainer() {
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);
        Optional<Trainer> foundTrainerOptional = facade.selectTrainer(testTrainer.getUsername());

        assertTrue(foundTrainerOptional.isPresent());
        assertNotNull(foundTrainerOptional.get());
        assertEquals(testTrainer, foundTrainerOptional.get());
    }

    @AfterEach
    public void setAfter() {
        try {
            authenticationService.logOut();
        } catch (IllegalArgumentException ignored) {
        }
    }
}