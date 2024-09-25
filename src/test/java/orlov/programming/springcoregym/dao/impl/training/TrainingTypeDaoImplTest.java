package orlov.programming.springcoregym.dao.impl.training;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.TestConfig;
import orlov.programming.springcoregym.dao.impl.TestDaoConfig;
import orlov.programming.springcoregym.model.training.TrainingType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
class TrainingTypeDaoImplTest {

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    private TrainingType testTrainingType;

    private static final String TRAINING_TYPE_NAME = "testTrainingTypeName";

    @BeforeEach
    void setUp() {
        testTrainingType = TrainingType.builder()
                .trainingTypeName(TRAINING_TYPE_NAME)
                .build();
    }

    @Test
    void testCreate() {
        testTrainingType = trainingTypeDao.create(testTrainingType);

        assertNotNull(testTrainingType);
        assertNotNull(testTrainingType.getId());
    }

    @Test
    void testFindById() {
        testTrainingType = trainingTypeDao.create(testTrainingType);
        TrainingType foundTrainingType = trainingTypeDao.findById(testTrainingType.getId());

        assertNotNull(foundTrainingType);
        assertEquals(testTrainingType, foundTrainingType);
    }

    @Test
    void testDelete() {
        TrainingType trainingType = trainingTypeDao.create(testTrainingType);

        trainingTypeDao.deleteById(trainingType.getId());
        TrainingType deleted = trainingTypeDao.findById(trainingType.getId());
        assertNull(deleted);
    }

    @Test
    void testDelete_NonExistentTrainingType() {
        assertDoesNotThrow(() -> trainingTypeDao.deleteById(-1L));
    }

    @Test
    void testUpdate() {
        TrainingType savedTrainingType = trainingTypeDao.create(testTrainingType);

        String delim = "1";

        TrainingType diffTrainingType = TrainingType.builder()
                .trainingTypeName(savedTrainingType.getTrainingTypeName() + delim)
                .build();

        TrainingType updated = trainingTypeDao.update(diffTrainingType);

        assertNotEquals(updated, savedTrainingType);
        assertEquals(updated.getTrainingTypeName(), savedTrainingType.getTrainingTypeName() + delim);
    }

    @Test
    void testFindAll() {
        TrainingType trainingType1 = TrainingType.builder()
                .trainingTypeName("testTrainingType1")
                .build();

        TrainingType trainingType2 = TrainingType.builder()
                .trainingTypeName("testTrainingType2")
                .build();

        trainingTypeDao.create(trainingType1);
        trainingTypeDao.create(trainingType2);

        List<TrainingType> trainingTypeList = trainingTypeDao.findAll();

        assertNotNull(trainingTypeList);
        assertEquals(2, trainingTypeList.size());
    }

    @AfterEach
    public void setAfter(){
        for(TrainingType trainingType : trainingTypeDao.findAll()){
            trainingTypeDao.deleteById(trainingType.getId());
        }
    }
}
