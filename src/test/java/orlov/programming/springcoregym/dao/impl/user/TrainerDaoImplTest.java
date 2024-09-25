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
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
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
    void setUp(){
        testTrainingType = TrainingType.builder()
                .trainingTypeName(SPECIALIZATION)
                .build();

        testTrainingType = trainingTypeDao.create(testTrainingType);

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
    void testCreate() {
        testTrainer = trainerDao.create(testTrainer);

        assertNotNull(testTrainer);
        assertNotNull(testTrainer.getId());
    }

    @Test
    void testFindById() {
        testTrainer = trainerDao.create(testTrainer);
        Trainer foundTrainer = trainerDao.findById(testTrainer.getId());

        assertNotNull(foundTrainer);
        assertEquals(testTrainer, foundTrainer);
    }

    @Test
    void testFindByUsername() {
        Trainer trainer = trainerDao.create(testTrainer);

        Optional<Trainer> found = trainerDao.findByUsername(trainer.getUsername());
        assertTrue(found.isPresent());
        assertEquals(trainer, found.get());
    }

    @Test
    void testDelete() {
        Trainer trainer = trainerDao.create(testTrainer);

        trainerDao.deleteById(trainer.getId());
        Trainer deleted = trainerDao.findById(trainer.getId());
        assertNull(deleted);
    }

    @Test
    void testDelete_NonExistentTrainer() {
        assertDoesNotThrow(() -> trainerDao.deleteById(-1L));
    }

    @Test
    void testUpdate() {
        Trainer savedTrainer = trainerDao.create(testTrainer);

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

        assertNotEquals(updated, savedTrainer);
        assertEquals(updated.getUsername(), savedTrainer.getUsername() + delim);
        assertEquals(updated.getFirstName(), savedTrainer.getFirstName() + delim);
        assertEquals(updated.getLastName(), savedTrainer.getLastName() + delim);
        assertEquals(updated.getPassword(), savedTrainer.getPassword() + delim);
        assertEquals(updated.getIsActive(), !savedTrainer.getIsActive() );
        assertEquals(updated.getSpecialization().getTrainingTypeName(),
                savedTrainer.getSpecialization().getTrainingTypeName() + delim);
    }

    @Test
    void testFindAll() {
        Trainer trainer1 = Trainer.builder()
                .username("testUser1")
                .firstName("First1")
                .lastName("Last1")
                .password("pass1")
                .isActive(true)
                .specialization(testTrainingType)
                .build();

        Trainer trainer2 = Trainer.builder()
                .username("testUser2")
                .firstName("First2")
                .lastName("Last2")
                .password("pass2")
                .isActive(true)
                .specialization(testTrainingType)
                .build();

        trainerDao.create(trainer1);
        trainerDao.create(trainer2);

        List<Trainer> trainerList = trainerDao.findAll();

        assertNotNull(trainerList);
        assertEquals(2, trainerList.size());
    }

    @Test
    void givenNothing_whenFindByUsername_ThenException(){
        Optional<Trainer> optionalTrainer = trainerDao.findByUsername("");

        assertTrue(optionalTrainer.isEmpty());
    }

    @Test
    void whenGetTrainingsByDate_thenReturnTrainingsAndUsername(){
        TrainingType testTrainingType = TrainingType.builder()
                .trainingTypeName("testTrainingType1")
                .build();

        Trainee testTrainee = Trainee.builder()
                .username("testTrainee")
                .firstName("First1")
                .lastName("Last1")
                .password("pass1")
                .isActive(true)
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

        List<Training> foundTrainings =
                trainerDao.getTrainingsByDateAndUsername(LocalDate.MIN, LocalDate.MIN.plusDays(1), testTrainer.getUsername());

        assertNotNull(foundTrainings);
        assertEquals(2, foundTrainings.size());
    }

    @Test
    void given2TrainerWithoutTrainees_whenGetTrainersWithoutPassedTrainee_thenSuccess(){
        Trainee testTrainee = Trainee.builder()
                .username("testTrainee")
                .firstName("First1")
                .lastName("Last1")
                .password("pass1")
                .isActive(true)
                .build();

        Trainer testTrainer2 = Trainer.builder()
                .username(USERNAME + "1")
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .specialization(testTrainingType)
                .isActive(IS_ACTIVE)
                .build();

        testTrainer = trainerDao.create(testTrainer);
        testTrainer2 = trainerDao.create(testTrainer2);
        testTrainee = traineeDao.create(testTrainee);

        List<Trainer> trainers = trainerDao.getTrainersWithoutPassedTrainee(testTrainee);

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(testTrainer));
        assertTrue(trainers.contains(testTrainer2));
    }

    @Test
    void given2Trainers_whenFindByIds_thenSuccess(){
        Trainer testTrainer2 = Trainer.builder()
                .username(USERNAME + USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .specialization(testTrainingType)
                .isActive(IS_ACTIVE)
                .build();

        testTrainer = trainerDao.create(testTrainer);
        testTrainer2 = trainerDao.create(testTrainer2);

        List<Long> ids = List.of(testTrainer.getId(), testTrainer2.getId());

        List<Trainer> trainers = trainerDao.findByIds(ids);

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(testTrainer));
        assertTrue(trainers.contains(testTrainer2));
    }

    @Test
    void givenNullIds_whenFindByIds_thenEmptyList(){
        List<Trainer> trainers = trainerDao.findByIds(null);

        assertNotNull(trainers);
        assertEquals(0, trainers.size());
    }

    @Test
    void givenEmptyIds_whenFindByIds_thenEmptyList(){
        List<Trainer> trainers = trainerDao.findByIds(List.of());

        assertNotNull(trainers);
        assertEquals(0, trainers.size());
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
    }
}
