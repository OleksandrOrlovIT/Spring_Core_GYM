package orlov.programming.springcoregym.dao.impl.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.TestConfig;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.model.user.Trainee;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class TraineeDaoImplTest {

    @Autowired
    private TraineeDao traineeDao;

    private Trainee testTrainee;

    private static final String USERNAME = "testUser";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "pass";
    private static final boolean IS_ACTIVE = true;

    @BeforeEach
    void setUp(){
        testTrainee = Trainee.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }

    @Test
    void testCreate() {
        testTrainee = traineeDao.create(testTrainee);

        assertNotNull(testTrainee);
        assertNotNull(testTrainee.getId());
    }

    @Test
    void testFindById() {
        testTrainee = traineeDao.create(testTrainee);
        Trainee foundTrainee = traineeDao.findById(testTrainee.getId());

        assertNotNull(foundTrainee);
        assertEquals(testTrainee, foundTrainee);
    }

    @Test
    void testFindByUsername() {
        Trainee trainee = traineeDao.create(testTrainee);

        Optional<Trainee> found = traineeDao.findByUsername(trainee.getUsername());
        assertTrue(found.isPresent());
        assertEquals(trainee, found.get());
    }

    @Test
    void testDelete() {
        Trainee trainee = traineeDao.create(testTrainee);

        traineeDao.deleteById(trainee.getId());
        Trainee deleted = traineeDao.findById(trainee.getId());
        assertNull(deleted);
    }

    @Test
    void testDelete_NonExistentTrainee() {
        assertDoesNotThrow(() -> traineeDao.deleteById(-1L));
    }

    @Test
    void testUpdate() {
        Trainee savedTrainee = traineeDao.create(testTrainee);

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
        assertEquals(updated.getIsActive(), !savedTrainee.getIsActive() );
    }

    @Test
    void testFindAll() {
        Trainee trainee1 = Trainee.builder()
                .username(USERNAME + 1)
                .firstName(FIRST_NAME + 1)
                .lastName(LAST_NAME + 1)
                .password(PASSWORD + 1)
                .isActive(IS_ACTIVE)
                .build();

        Trainee trainee2 = Trainee.builder()
                .username(USERNAME + 2)
                .firstName(FIRST_NAME + 2)
                .lastName(LAST_NAME + 2)
                .password(PASSWORD + 2)
                .isActive(IS_ACTIVE)
                .build();

        traineeDao.create(trainee1);
        traineeDao.create(trainee2);

        List<Trainee> traineeList = traineeDao.findAll();

        assertNotNull(traineeList);
        assertEquals(2, traineeList.size());
    }

    @Test
    void givenNothing_whenFindByUsername_ThenException(){
        Optional<Trainee> optionalTrainee = traineeDao.findByUsername("");

        assertTrue(optionalTrainee.isEmpty());
    }

    @AfterEach
    public void setAfter(){
        for(Trainee trainee : traineeDao.findAll()){
            traineeDao.deleteById(trainee.getId());
        }
    }
}
