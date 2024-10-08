package orlov.programming.springcoregym.facade.training.impl;

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
@Sql(scripts = "/sql/training/populate_trainings.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
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
        assertEquals(1, traineeDao.getAll().size());
        assertEquals(1, trainingTypeDao.getAll().size());
        assertEquals(1, trainerDao.getAll().size());
        assertEquals(2, trainingDao.getAll().size());

        testTrainingType = trainingTypeDao.getAll().get(0);
        testTrainee = traineeDao.getAll().get(0);
        testTrainer = trainerDao.getAll().get(0);

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
        authenticationService.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        Training savedTraining = facade.addTraining(testTraining);
        assertEquals(testTraining, savedTraining);
    }

    @AfterEach
    public void setAfter() {
        try {
            authenticationService.logOut();
        } catch (IllegalArgumentException ignored) {
        }
    }
}