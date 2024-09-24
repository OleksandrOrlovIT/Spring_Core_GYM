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
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.model.user.Trainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TrainerDaoImplTest {
    @Autowired
    private TrainerDao trainerDao;

    private Trainer testTrainer;

    private static final String USERNAME = "testUser";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "pass";
    private static final String SPECIALIZATION = "specialization";
    private static final boolean IS_ACTIVE = true;

    @BeforeEach
    void setUp(){
        testTrainer = Trainer.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .specialization(SPECIALIZATION)
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

        Trainer diffTrainer = Trainer.builder()
                .username(savedTrainer.getUsername() + delim)
                .firstName(savedTrainer.getFirstName() + delim)
                .lastName(savedTrainer.getLastName() + delim)
                .password(savedTrainer.getPassword() + delim)
                .isActive(!savedTrainer.getIsActive())
                .specialization(savedTrainer.getSpecialization() + delim)
                .build();

        Trainer updated = trainerDao.update(diffTrainer);

        assertNotEquals(updated, savedTrainer);
        assertEquals(updated.getUsername(), savedTrainer.getUsername() + delim);
        assertEquals(updated.getFirstName(), savedTrainer.getFirstName() + delim);
        assertEquals(updated.getLastName(), savedTrainer.getLastName() + delim);
        assertEquals(updated.getPassword(), savedTrainer.getPassword() + delim);
        assertEquals(updated.getIsActive(), !savedTrainer.getIsActive() );
        assertEquals(updated.getSpecialization(), savedTrainer.getSpecialization() + delim);
    }

    @Test
    void testFindAll() {
        Trainer trainer1 = Trainer.builder()
                .username("testUser1")
                .firstName("First1")
                .lastName("Last1")
                .password("pass1")
                .isActive(true)
                .specialization("Specialization")
                .build();

        Trainer trainer2 = Trainer.builder()
                .username("testUser2")
                .firstName("First2")
                .lastName("Last2")
                .password("pass2")
                .isActive(true)
                .specialization("Specialization")
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

    @AfterEach
    public void setAfter(){
        for(Trainer trainer : trainerDao.findAll()){
            trainerDao.deleteById(trainer.getId());
        }
    }
}
