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
import orlov.programming.springcoregym.dto.trainee.TraineeTrainingDTO;
import orlov.programming.springcoregym.facade.user.TraineeFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Sql(scripts = "/sql/trainee/populate_trainee.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class TraineeFacadeTest {

    @Autowired
    private TraineeFacade facade;

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
        assertEquals(1, traineeDao.getAll().size());
        assertEquals(1, trainingTypeDao.getAll().size());
        assertEquals(1, trainerDao.getAll().size());
        assertEquals(2, trainingDao.getAll().size());

        testTrainingType = trainingTypeDao.getAll().get(0);
        testTrainer = trainerDao.getAll().get(0);
        testTraining = trainingDao.getAll().get(0);
        testTrainee = traineeDao.getAll().get(0);
    }

    @Test
    @Transactional
    void createTrainee() {
        String delim = "1";
        Trainee customTrainee = Trainee.builder()
                .firstName(testTrainee.getFirstName() + delim)
                .lastName(testTrainee.getLastName() + delim)
                .password(testTrainee.getPassword() + delim)
                .isActive(false)
                .build();
        Optional<Trainee> savedTrainee = facade.createTrainee(customTrainee);

        assertTrue(savedTrainee.isPresent());
        assertEquals(savedTrainee.get().getFirstName(), customTrainee.getFirstName());
        assertEquals(savedTrainee.get().getLastName(), customTrainee.getLastName());
    }

    @Test
    @Transactional
    void updateTrainee() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        String updatedAddress = "newAddress";
        testTrainee.setAddress(updatedAddress);

        Optional<Trainee> updatedTrainee = facade.updateTrainee(testTrainee);

        assertTrue(updatedTrainee.isPresent());
        assertEquals(updatedTrainee.get().getAddress(), updatedAddress);
    }

    @Test
    @Transactional
    void deleteTrainee() {
        String delim = "1";
        Trainee customTrainee = Trainee.builder()
                .firstName(testTrainee.getFirstName() + delim)
                .lastName(testTrainee.getLastName() + delim)
                .password(testTrainee.getPassword() + delim)
                .isActive(false)
                .build();

        Trainee createdTrainee = facade.createTrainee(customTrainee).orElseThrow();

        authenticationService.authenticateUser(createdTrainee.getUsername(), createdTrainee.getPassword(), true);

        facade.deleteTrainee(createdTrainee.getUsername());

        assertTrue(facade.selectTrainee(createdTrainee.getUsername()).isEmpty());
    }

    @Test
    void selectTrainee() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        Optional<Trainee> foundTrainee = facade.selectTrainee(testTrainee.getUsername());
        assertTrue(foundTrainee.isPresent());
        assertEquals(testTrainee, foundTrainee.get());
    }

    @Test
    void createTraineeGivenNullThenEmpty() {
        Optional<Trainee> savedTrainee = facade.createTrainee(null);

        assertTrue(savedTrainee.isEmpty());
    }

    @Test
    void isTraineeUsernamePasswordMatchGivenNullThenFalse() {
        boolean res = facade.isTraineeUsernamePasswordMatch(null, null);

        assertFalse(res);
    }

    @Test
    void isTraineeUsernamePasswordMatchGivenValidThenTrue() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        boolean res = facade.isTraineeUsernamePasswordMatch(testTrainee.getUsername(), testTrainee.getPassword());

        assertTrue(res);
    }

    @Test
    void updateTraineeGivenNullThenEmpty() {
        Optional<Trainee> savedTrainee = facade.updateTrainee(null);

        assertTrue(savedTrainee.isEmpty());
    }

    @Test
    void deleteTraineeGivenNullthenEmpty() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        assertDoesNotThrow(() -> facade.deleteTrainee(null));
    }

    @Test
    void changeTraineePasswordGivenNullThenEmpty() {
        Optional<Trainee> savedTrainee = facade.changeTraineePassword(null, null);

        assertTrue(savedTrainee.isEmpty());
    }

    @Test
    @Transactional
    void changeTraineePasswordGivenWrongUserThenSuccess() {
        Trainee newTrainee = Trainee.builder()
                .username("testTrainee")
                .firstName("NewTrainee")
                .lastName("NewTrainee")
                .password("pass1")
                .isActive(true)
                .build();

        Optional<Trainee> savedTraineeOptional = facade.createTrainee(newTrainee);
        assertTrue(savedTraineeOptional.isPresent());
        Trainee savedTrainee = savedTraineeOptional.get();
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        String newPassword = "newPassword";

        Optional<Trainee> trainee = facade.changeTraineePassword(savedTrainee.getUsername(), newPassword);
        assertTrue(trainee.isEmpty());
    }

    @Test
    @Transactional
    void changeTraineePasswordGivenValidThenSuccess() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        String newPassword = "newPassword";

        Optional<Trainee> savedTrainee = facade.changeTraineePassword(testTrainee.getUsername(), newPassword);

        assertTrue(savedTrainee.isPresent());
        assertEquals(newPassword, savedTrainee.get().getPassword());
    }

    @Test
    void activateTraineeGivenNotFoundThenEmpty() {
        Optional<Trainee> trainee = facade.activateTrainee(testTrainee.getUsername());

        assertTrue(trainee.isEmpty());
    }

    @Test
    @Transactional
    void activateTraineeGivenValidThenSuccess() {
        String delim = "1";
        Trainee customTrainee = Trainee.builder()
                .firstName(testTrainee.getFirstName() + delim)
                .lastName(testTrainee.getLastName() + delim)
                .password(testTrainee.getPassword() + delim)
                .isActive(false)
                .build();

        customTrainee = facade.createTrainee(customTrainee).orElseThrow();
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        Optional<Trainee> activatedTrainee = facade.activateTrainee(customTrainee.getUsername());

        assertTrue(activatedTrainee.isPresent());
        assertTrue(activatedTrainee.get().getIsActive());
    }

    @Test
    void getTraineeTrainingsByTraineeTrainingDTOGivenValidThenSuccess() {
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        TraineeTrainingDTO traineeTrainingDTO = new TraineeTrainingDTO(testTraining.getTrainingDate(),
                testTraining.getTrainingDate().plusDays(1), testTrainee.getUsername(), testTrainingType.getTrainingTypeName());
        List<Training> trainings = facade.getTraineeTrainingsByTraineeTrainingDTO(traineeTrainingDTO);

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        assertEquals(trainings, trainingDao.getAll());
    }

    @Test
    void updateTraineeTrainersGivenInvalidThenNothing() {
        assertDoesNotThrow(() -> facade.updateTraineeTrainers(null, null));
    }

    @Test
    @Transactional
    void updateTraineeTrainersGivenValidThenSuccess() {
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        assertDoesNotThrow(() -> facade.updateTraineeTrainers(testTrainee.getId(), List.of(testTrainer.getId())));

        Optional<Trainee> selectedTraineeOptional = facade.selectTrainee(testTrainee.getUsername());

        assertTrue(selectedTraineeOptional.isPresent());
        assertNotNull(selectedTraineeOptional.get().getTrainers());
    }

    @AfterEach
    public void setAfter() {
        try {
            authenticationService.logOut();
        } catch (IllegalArgumentException ignored) {
        }
    }
}