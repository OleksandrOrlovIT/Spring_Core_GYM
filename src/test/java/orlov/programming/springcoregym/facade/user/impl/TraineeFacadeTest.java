package orlov.programming.springcoregym.facade.user.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.TestConfig;
import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
import orlov.programming.springcoregym.dao.impl.training.TrainingTypeDao;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.facade.user.TraineeFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
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
        testTrainingType = TrainingType.builder()
                .trainingTypeName("testTrainingType1")
                .build();

        testTrainee = Trainee.builder()
                .username("testTrainee")
                .firstName("First1Trainee")
                .lastName("Last1Trainee")
                .password("pass1")
                .isActive(true)
                .build();

        testTrainer = Trainer.builder()
                .username("testTrainer")
                .firstName("First1Trainer")
                .lastName("Last1Trainer")
                .password("pass1")
                .isActive(true)
                .specialization(testTrainingType)
                .build();

        testTraining = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName("TRAINING_NAME")
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();
    }

    @Test
    void createTrainee() {
        Optional<Trainee> savedTrainee = facade.createTrainee(testTrainee);

        assertTrue(savedTrainee.isPresent());
        assertEquals(savedTrainee.get().getFirstName(), testTrainee.getFirstName());
        assertEquals(savedTrainee.get().getLastName(), testTrainee.getLastName());
    }

    @Test
    void updateTrainee() {
        Optional<Trainee> savedTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(savedTraineeOptional.isPresent());
        Trainee savedTrainee = savedTraineeOptional.get();
        authenticationService.authenticateUser(savedTrainee.getUsername(), savedTrainee.getPassword(), true);

        String updatedAddress = "newAddress";
        savedTrainee.setAddress(updatedAddress);

        Optional<Trainee> updatedTrainee = facade.updateTrainee(savedTrainee);

        assertTrue(updatedTrainee.isPresent());
        assertEquals(updatedTrainee.get().getAddress(), updatedAddress);
    }

    @Test
    void deleteTrainee() {
        Optional<Trainee> savedTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(savedTraineeOptional.isPresent());
        Trainee savedTrainee = savedTraineeOptional.get();
        authenticationService.authenticateUser(savedTrainee.getUsername(), savedTrainee.getPassword(), true);

        Optional<Trainee> foundTrainee = facade.selectTrainee(savedTrainee.getUsername());
        assertTrue(foundTrainee.isPresent());
        facade.deleteTrainee(foundTrainee.get().getUsername());

        assertTrue(facade.selectTrainee(savedTrainee.getUsername()).isEmpty());
    }

    @Test
    void selectTrainee() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainee> savedTrainee = facade.createTrainee(testTrainee);
        assertTrue(savedTrainee.isPresent());

        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        Optional<Trainee> foundTrainee = facade.selectTrainee(savedTrainee.get().getUsername());
        assertTrue(foundTrainee.isPresent());
        assertEquals(savedTrainee.get(), foundTrainee.get());
    }

    @Test
    void givenNull_whenCreateTrainee_thenEmpty(){
        Optional<Trainee> savedTrainee = facade.createTrainee(null);

        assertTrue(savedTrainee.isEmpty());
    }

    @Test
    void givenNull_whenTraineeUsernamePasswordMatch_thenFalse(){
        boolean res = facade.traineeUsernamePasswordMatch(null, null);

        assertFalse(res);
    }

    @Test
    void givenValid_whenTraineeUsernamePasswordMatch_thenTrue(){
        Optional<Trainee> savedTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(savedTraineeOptional.isPresent());
        Trainee savedTrainee = savedTraineeOptional.get();

        authenticationService.authenticateUser(savedTrainee.getUsername(), savedTrainee.getPassword(), true);
        boolean res = facade.traineeUsernamePasswordMatch(savedTrainee.getUsername(), savedTrainee.getPassword());

        assertTrue(res);
    }

    @Test
    void givenNull_whenUpdateTrainee_thenEmpty(){
        Optional<Trainee> savedTrainee = facade.updateTrainee(null);

        assertTrue(savedTrainee.isEmpty());
    }

    @Test
    void givenNull_whenDeleteTrainee_thenEmpty(){
        facade.createTrainee(testTrainee);
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        assertDoesNotThrow(() -> facade.deleteTrainee(null));
    }

    @Test
    void givenNull_whenChangeTraineePassword_thenEmpty(){
        Optional<Trainee> savedTrainee = facade.changeTraineePassword(null, null);

        assertTrue(savedTrainee.isEmpty());
    }

    @Test
    void givenWrongUser_whenChangeTraineePassword_thenSuccess(){
        Optional<Trainee> createdTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(createdTraineeOptional.isPresent());
        testTrainee = createdTraineeOptional.get();
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
    void givenValid_whenChangeTraineePassword_thenSuccess(){
        Optional<Trainee> testTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(testTraineeOptional.isPresent());
        testTrainee = testTraineeOptional.get();
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        String newPassword = "newPassword";

        Optional<Trainee> savedTrainee = facade.changeTraineePassword(testTrainee.getUsername(), newPassword);

        assertTrue(savedTrainee.isPresent());
        assertEquals(newPassword, savedTrainee.get().getPassword());
    }

    @Test
    void givenNotFound_whenActivateTrainee_thenEmpty(){
        Optional<Trainee> trainee = facade.activateTrainee(testTrainee.getUsername());

        assertTrue(trainee.isEmpty());
    }

    @Test
    void givenValid_whenActivateTrainee_thenSuccess(){
        testTrainee.setIsActive(false);
        Optional<Trainee> testTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(testTraineeOptional.isPresent());
        testTrainee = testTraineeOptional.get();
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        Optional<Trainee> activatedTrainee = facade.activateTrainee(testTrainee.getUsername());

        assertTrue(activatedTrainee.isPresent());
        assertTrue(activatedTrainee.get().getIsActive());
    }

    @Test
    void givenValid_whenGetTraineeTrainingsByDateTraineeNameTrainingType_thenSuccess(){
        Optional<Trainee> testTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(testTraineeOptional.isPresent());
        testTrainee = testTraineeOptional.get();
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainer.setSpecialization(testTrainingType);
        testTrainer = trainerDao.create(testTrainer);
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);
        testTraining = trainingDao.create(testTraining);

        Training training2 = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName("TRAINING_NAME")
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();
        training2 = trainingDao.create(training2);

        TraineeTrainingDTO traineeTrainingDTO = new TraineeTrainingDTO(testTraining.getTrainingDate(),
                testTraining.getTrainingDate(), testTrainee.getUsername(), testTrainingType.getTrainingTypeName());
        List<Training> trainings = facade.getTraineeTrainingsByDateTraineeNameTrainingType(traineeTrainingDTO);

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(testTraining));
        assertTrue(trainings.contains(training2));
    }

    @Test
    void givenInvalid_whenUpdateTraineeTrainers_thenNothing(){
        assertDoesNotThrow(() -> facade.updateTraineeTrainers(null, null));
    }

    @Test
    void givenValid_whenUpdateTraineeTrainers_thenSuccess(){
        Optional<Trainee> testTraineeOptional = facade.createTrainee(testTrainee);
        assertTrue(testTraineeOptional.isPresent());
        testTrainee = testTraineeOptional.get();

        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        testTrainer = trainerDao.create(testTrainer);

        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        assertDoesNotThrow(() -> facade.updateTraineeTrainers(testTrainee.getId(), List.of(testTrainer.getId())));

        Optional<Trainee> selectedTraineeOptional = facade.selectTrainee(testTrainee.getUsername());

        assertTrue(selectedTraineeOptional.isPresent());
        assertNotNull(selectedTraineeOptional.get().getTrainers());
    }

    @AfterEach
    public void setAfter(){
        for(Training training : trainingDao.findAll()){
            trainingDao.deleteById(training.getId());
        }

        for(Trainer trainer : trainerDao.findAll()){
            trainerDao.deleteById(trainer.getId());
        }

        for(Trainee trainee : traineeDao.findAll()){
            traineeDao.deleteById(trainee.getId());
        }

        for(TrainingType trainingType : trainingTypeDao.findAll()){
            trainingTypeDao.deleteById(trainingType.getId());
        }

        try {
            authenticationService.logOut();
        } catch (IllegalArgumentException ignored){}
    }
}