package orlov.programming.springcoregym.facade.training.impl;

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
import orlov.programming.springcoregym.facade.training.TrainingFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class TrainingFacadeTest {

    @Autowired
    private TrainingFacade facade;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingDao trainingDao;

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
    void addTraining() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        testTrainer = trainerDao.create(testTrainer);
        testTrainee = traineeDao.create(testTrainee);

        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainer(testTrainer);
        testTraining.setTrainee(testTrainee);

        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        Training savedTraining = facade.addTraining(testTraining);
        assertEquals(testTraining, savedTraining);
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

        try {
            authenticationService.logOut();
        } catch (IllegalArgumentException ignored) {
        }
    }
}