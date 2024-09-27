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
import orlov.programming.springcoregym.facade.user.TrainerFacade;
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
    void givenNull_whenCreateTrainer_thenEmpty(){
        Optional<Trainer> savedTrainer = facade.createTrainer(null);
        assertTrue(savedTrainer.isEmpty());
    }

    @Test
    void givenNull_whenTrainerUsernamePasswordMatch_thenFalse(){
        boolean res = facade.trainerUsernamePasswordMatch(null, null);

        assertFalse(res);
    }

    @Test
    void givenValid_whenTrainerUsernamePasswordMatch_thenTrue(){
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> createdTrainer = facade.createTrainer(testTrainer);
        assertTrue(createdTrainer.isPresent());
        testTrainer = createdTrainer.get();

        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);
        boolean res = facade.trainerUsernamePasswordMatch(testTrainer.getUsername(), testTrainer.getPassword());

        assertTrue(res);
    }

    @Test
    void givenNull_whenUpdateTrainer_thenEmpty(){
        Optional<Trainer> trainer = facade.updateTrainer(null);

        assertTrue(trainer.isEmpty());
    }

    @Test
    void givenNull_whenSelectTrainer_thenEmpty(){
        Optional<Trainer> trainer = facade.selectTrainer(null);

        assertTrue(trainer.isEmpty());
    }

    @Test
    void givenNotFound_whenActivateTrainer_thenEmpty(){
        Optional<Trainer> trainer = facade.activateTrainer(testTrainer.getUsername());

        assertTrue(trainer.isEmpty());
    }

    @Test
    void givenValid_whenActivateTrainer_thenSuccess(){
        testTrainer.setIsActive(false);
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> createdTrainer = facade.createTrainer(testTrainer);
        assertTrue(createdTrainer.isPresent());
        testTrainer = createdTrainer.get();
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        Optional<Trainer> trainer = facade.activateTrainer(testTrainer.getUsername());

        assertTrue(trainer.isPresent());
        assertTrue(trainer.get().getIsActive());
    }

    @Test
    void givenNull_whenChangeTrainerPassword_thenEmpty(){
        Optional<Trainer> savedTrainer = facade.changeTrainerPassword(null, null);

        assertTrue(savedTrainer.isEmpty());
    }

    @Test
    void givenValid_whenChangeTrainerPassword_thenSuccess(){
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> createdTrainer = facade.createTrainer(testTrainer);
        assertTrue(createdTrainer.isPresent());
        testTrainer = createdTrainer.get();
        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        String newPassword = "newPassword";

        Optional<Trainer> savedTrainer = facade.changeTrainerPassword(testTrainer.getUsername(), newPassword);

        assertTrue(savedTrainer.isPresent());
        assertEquals(newPassword, savedTrainer.get().getPassword());
    }

    @Test
    void givenValid_whenGetTrainingsByDateTrainerName_thenSuccess(){
        testTrainee = traineeDao.create(testTrainee);
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> createdTrainer = facade.createTrainer(testTrainer);
        assertTrue(createdTrainer.isPresent());
        testTrainer = createdTrainer.get();

        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);

        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
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

        List<Training> trainings = facade.getTrainingsByDateTrainerName(
                testTraining.getTrainingDate(),testTraining.getTrainingDate(), testTrainer.getUsername());

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(testTraining));
        assertTrue(trainings.contains(training2));
    }

    @Test
    void givenValid_whenGetTrainersWithoutPassedTrainee_thenSuccess(){
        testTrainee = traineeDao.create(testTrainee);
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> createdTrainer = facade.createTrainer(testTrainer);
        assertTrue(createdTrainer.isPresent());
        testTrainer = createdTrainer.get();

        Trainer trainer2 = Trainer.builder()
                .firstName("First1Trainer2")
                .lastName("Last1Trainer2")
                .password("pass1")
                .isActive(true)
                .specialization(testTrainingType)
                .build();
        Optional<Trainer> createdTrainer2 = facade.createTrainer(trainer2);
        assertTrue(createdTrainer2.isPresent());
        trainer2 = createdTrainer2.get();

        authenticationService.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        List<Trainer> trainers = facade.getTrainersWithoutPassedTrainee(testTrainee.getUsername());

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(testTrainer));
        assertTrue(trainers.contains(trainer2));
    }

    @Test
    void createTrainer() {
        trainingTypeDao.create(testTrainingType);
        Optional<Trainer> savedTrainer = facade.createTrainer(testTrainer);

        assertTrue(savedTrainer.isPresent());
        assertEquals(savedTrainer.get().getFirstName(), testTrainer.getFirstName());
        assertEquals(savedTrainer.get().getLastName(), testTrainer.getLastName());
    }

    @Test
    void updateTrainer() {
        String addition = "1";
        TrainingType updatedTrainingType = TrainingType.builder()
                .trainingTypeName("newSpecialization")
                .build();


        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> createdTrainer = facade.createTrainer(testTrainer);
        assertTrue(createdTrainer.isPresent());
        Trainer savedTrainer = createdTrainer.get();

        updatedTrainingType = trainingTypeDao.create(updatedTrainingType);
        savedTrainer.setSpecialization(updatedTrainingType);

        authenticationService.authenticateUser(savedTrainer.getUsername(), savedTrainer.getPassword(), false);
        Optional<Trainer> updatedTrainerOptional = facade.updateTrainer(savedTrainer);
        assertTrue(updatedTrainerOptional.isPresent());
        Trainer updatedTrainer = updatedTrainerOptional.get();

        assertNotNull(updatedTrainer);
        assertEquals(updatedTrainer.getSpecialization(), updatedTrainingType);
    }

    @Test
    void selectTrainer() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Optional<Trainer> savedTrainerOptional = facade.createTrainer(testTrainer);
        assertTrue(savedTrainerOptional.isPresent());
        Trainer savedTrainer = savedTrainerOptional.get();

        authenticationService.authenticateUser(savedTrainer.getUsername(), savedTrainer.getPassword(), false);
        Optional<Trainer> foundTrainerOptional = facade.selectTrainer(savedTrainer.getUsername());

        assertTrue(foundTrainerOptional.isPresent());
        assertNotNull(foundTrainerOptional.get());
        assertEquals(savedTrainer, foundTrainerOptional.get());
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