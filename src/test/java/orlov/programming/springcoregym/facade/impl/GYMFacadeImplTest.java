package orlov.programming.springcoregym.facade.impl;

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
import orlov.programming.springcoregym.facade.GYMFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class GYMFacadeImplTest {

    @Autowired
    private GYMFacade facade;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingDao trainingDao;

    private Training testTraining;
    private TrainingType testTrainingType;
    private Trainer testTrainer;
    private Trainee testTrainee;

    private static final String TRAINING_NAME = "testTrainingName";

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
                .trainingName(TRAINING_NAME)
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();
    }

    @Test
    void createTrainee() {
        Trainee savedTrainee = facade.createTrainee(testTrainee);

        assertNotNull(savedTrainee);
        assertEquals(savedTrainee.getFirstName(), testTrainee.getFirstName());
        assertEquals(savedTrainee.getLastName(), testTrainee.getLastName());
    }

    @Test
    void updateTrainee() {
        Trainee savedTrainee = facade.createTrainee(testTrainee);
        facade.authenticateUser(savedTrainee.getUsername(), savedTrainee.getPassword(), true);

        String updatedAddress = "newAddress";
        savedTrainee.setAddress(updatedAddress);

        Trainee updatedTrainee = facade.updateTrainee(savedTrainee);

        assertNotNull(updatedTrainee);
        assertEquals(updatedTrainee.getAddress(), updatedAddress);
    }

    @Test
    void deleteTrainee() {
        Trainee savedTrainee = facade.createTrainee(testTrainee);
        facade.authenticateUser(savedTrainee.getUsername(), savedTrainee.getPassword(), true);

        Trainee foundTrainee = facade.selectTrainee(savedTrainee.getUsername());
        facade.deleteTrainee(foundTrainee.getUsername());

        assertNull(facade.selectTrainee(savedTrainee.getUsername()));
    }

    @Test
    void selectTrainee() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Trainee savedTrainee = facade.createTrainee(testTrainee);

        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        Trainee foundTrainee = facade.selectTrainee(savedTrainee.getUsername());
        assertNotNull(foundTrainee);
        assertEquals(savedTrainee, foundTrainee);
    }

    @Test
    void createTrainer() {
        trainingTypeDao.create(testTrainingType);
        Trainer savedTrainer = facade.createTrainer(testTrainer);

        assertNotNull(savedTrainer);
        assertEquals(savedTrainer.getFirstName(), testTrainer.getFirstName());
        assertEquals(savedTrainer.getLastName(), testTrainer.getLastName());
    }

    @Test
    void updateTrainer() {
        String addition = "1";
        TrainingType updatedTrainingType = TrainingType.builder()
                .trainingTypeName("newSpecialization")
                .build();


        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Trainer savedTrainer = facade.createTrainer(testTrainer);

        updatedTrainingType = trainingTypeDao.create(updatedTrainingType);
        savedTrainer.setSpecialization(updatedTrainingType);

        facade.authenticateUser(savedTrainer.getUsername(), savedTrainer.getPassword(), false);
        Trainer updatedTrainer = facade.updateTrainer(savedTrainer);

        assertNotNull(updatedTrainer);
        assertEquals(updatedTrainer.getSpecialization(), updatedTrainingType);
    }

    @Test
    void selectTrainer() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        Trainer savedTrainer = facade.createTrainer(testTrainer);
        facade.authenticateUser(savedTrainer.getUsername(), savedTrainer.getPassword(), false);
        Trainer foundTrainer = facade.selectTrainer(savedTrainer.getUsername());

        assertNotNull(foundTrainer);
        assertEquals(savedTrainer, foundTrainer);
    }

    @Test
    void createTraining() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);
        testTrainee = facade.createTrainee(testTrainee);

        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainer(testTrainer);
        testTraining.setTrainee(testTrainee);

        System.out.println("TestTraining = " + testTraining);

        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        Training savedTraining = facade.addTraining(testTraining);
        assertEquals(testTraining, savedTraining);
    }

    @Test
    void givenNull_whenCreateTrainee_thenNull(){
        Trainee savedTrainee = facade.createTrainee(null);

        assertNull(savedTrainee);
    }

    @Test
    void givenNull_whenTraineeUsernamePasswordMatch_thenFalse(){
        boolean res = facade.traineeUsernamePasswordMatch(null, null);

        assertFalse(res);
    }

    @Test
    void givenValid_whenTraineeUsernamePasswordMatch_thenTrue(){
        Trainee savedTrainee = facade.createTrainee(testTrainee);

        facade.authenticateUser(savedTrainee.getUsername(), savedTrainee.getPassword(), true);
        boolean res = facade.traineeUsernamePasswordMatch(savedTrainee.getUsername(), savedTrainee.getPassword());

        assertTrue(res);
    }

    @Test
    void givenNull_whenUpdateTrainee_thenNull(){
        Trainee savedTrainee = facade.updateTrainee(null);

        assertNull(savedTrainee);
    }

    @Test
    void givenNull_whenDeleteTrainee_thenNull(){
        facade.createTrainee(testTrainee);
        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        assertDoesNotThrow(() -> facade.deleteTrainee(null));
    }

    @Test
    void givenNull_whenChangeTraineePassword_thenNull(){
        Trainee savedTrainee = facade.changeTraineePassword(null, null);

        assertNull(savedTrainee);
    }

    @Test
    void givenWrongUser_whenChangeTraineePassword_thenSuccess(){
        testTrainee = facade.createTrainee(testTrainee);
        Trainee newTrainee = Trainee.builder()
                .username("testTrainee")
                .firstName("NewTrainee")
                .lastName("NewTrainee")
                .password("pass1")
                .isActive(true)
                .build();
        Trainee savedTrainee = facade.createTrainee(newTrainee);
        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        String newPassword = "newPassword";

        Trainee trainee = facade.changeTraineePassword(savedTrainee.getUsername(), newPassword);
        assertNull(trainee);
    }

    @Test
    void givenValid_whenChangeTraineePassword_thenSuccess(){
        testTrainee = facade.createTrainee(testTrainee);
        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        String newPassword = "newPassword";

        Trainee savedTrainee = facade.changeTraineePassword(testTrainee.getUsername(), newPassword);

        assertNotNull(savedTrainee);
        assertEquals(newPassword, savedTrainee.getPassword());
    }

    @Test
    void givenNotFound_whenActivateTrainee_thenNull(){
        Trainee trainee = facade.activateTrainee(testTrainee.getUsername());

        assertNull(trainee);
    }

    @Test
    void givenValid_whenActivateTrainee_thenSuccess(){
        testTrainee.setIsActive(false);
        testTrainee = facade.createTrainee(testTrainee);
        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);

        Trainee trainee = facade.activateTrainee(testTrainee.getUsername());

        assertNotNull(trainee);
        assertTrue(trainee.getIsActive());
    }

    @Test
    void givenValid_whenGetTraineeTrainingsByDateTraineeNameTrainingType_thenSuccess(){
        testTrainee = facade.createTrainee(testTrainee);
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);
        facade.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);
        testTraining = facade.addTraining(testTraining);

        Training training2 = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName(TRAINING_NAME)
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();
        training2 = facade.addTraining(training2);

        List<Training> trainings = facade.getTraineeTrainingsByDateTraineeNameTrainingType(
                        testTraining.getTrainingDate(),testTraining.getTrainingDate(),
                        testTrainee.getUsername(), testTrainingType.getTrainingTypeName());

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
        testTrainee = facade.createTrainee(testTrainee);

        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);

        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        assertDoesNotThrow(() -> facade.updateTraineeTrainers(testTrainee.getId(), List.of(testTrainer.getId())));

        testTrainee = facade.selectTrainee(testTrainee.getUsername());

        assertNotNull(testTrainee.getTrainers());
    }

    @Test
    void givenNull_whenCreateTrainer_thenNull(){
        Trainer savedTrainer = facade.createTrainer(null);
        assertNull(savedTrainer);
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
        testTrainer = facade.createTrainer(testTrainer);

        facade.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);
        boolean res = facade.trainerUsernamePasswordMatch(testTrainer.getUsername(), testTrainer.getPassword());

        assertTrue(res);
    }

    @Test
    void givenNull_whenUpdateTrainer_thenNull(){
        Trainer trainer = facade.updateTrainer(null);

        assertNull(trainer);
    }

    @Test
    void givenNull_whenSelectTrainer_thenNull(){
        Trainer trainer = facade.selectTrainer(null);

        assertNull(trainer);
    }

    @Test
    void givenNotFound_whenActivateTrainer_thenNull(){
        Trainer trainer = facade.activateTrainer(testTrainer.getUsername());

        assertNull(trainer);
    }

    @Test
    void givenValid_whenActivateTrainer_thenSuccess(){
        testTrainer.setIsActive(false);
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);
        facade.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        Trainer trainer = facade.activateTrainer(testTrainer.getUsername());

        assertNotNull(trainer);
        assertTrue(trainer.getIsActive());
    }

    @Test
    void givenNull_whenChangeTrainerPassword_thenNull(){
        Trainer savedTrainer = facade.changeTrainerPassword(null, null);

        assertNull(savedTrainer);
    }

    @Test
    void givenValid_whenChangeTrainerPassword_thenSuccess(){
        testTrainingType = trainingTypeDao.create(testTrainingType);
        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);
        facade.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        String newPassword = "newPassword";

        Trainer savedTrainer = facade.changeTrainerPassword(testTrainer.getUsername(), newPassword);

        assertNotNull(savedTrainer);
        assertEquals(newPassword, savedTrainer.getPassword());
    }

    @Test
    void givenValid_whenGetTrainingsByDateTrainerName_thenSuccess(){
        testTrainee = facade.createTrainee(testTrainee);
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);

        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);

        facade.authenticateUser(testTrainee.getUsername(), testTrainee.getPassword(), true);
        testTraining = facade.addTraining(testTraining);

        Training training2 = Training.builder()
                .trainee(testTrainee)
                .trainer(testTrainer)
                .trainingName(TRAINING_NAME)
                .trainingType(testTrainingType)
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();
        training2 = facade.addTraining(training2);

        List<Training> trainings = facade.getTrainingsByDateTrainerName(
                testTraining.getTrainingDate(),testTraining.getTrainingDate(), testTrainer.getUsername());

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(testTraining));
        assertTrue(trainings.contains(training2));
    }

    @Test
    void givenValid_whenGetTrainersWithoutPassedTrainee_thenSuccess(){
        testTrainee = facade.createTrainee(testTrainee);
        testTrainingType = trainingTypeDao.create(testTrainingType);

        testTrainer.setSpecialization(testTrainingType);
        testTrainer = facade.createTrainer(testTrainer);

        Trainer trainer2 = Trainer.builder()
                .firstName("First1Trainer2")
                .lastName("Last1Trainer2")
                .password("pass1")
                .isActive(true)
                .specialization(testTrainingType)
                .build();
        trainer2 = facade.createTrainer(trainer2);

        facade.authenticateUser(testTrainer.getUsername(), testTrainer.getPassword(), false);

        List<Trainer> trainers = facade.getTrainersWithoutPassedTrainee(testTrainee.getUsername());

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(testTrainer));
        assertTrue(trainers.contains(trainer2));
    }

    @Test
    void givenNull_whenAuthenticateUser_thenNothing(){
        assertDoesNotThrow(() -> facade.authenticateUser(null, null, false));
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
            facade.logOut();
        } catch (IllegalArgumentException ignored){}
    }
}
