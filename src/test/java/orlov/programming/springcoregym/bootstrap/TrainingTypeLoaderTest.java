package orlov.programming.springcoregym.bootstrap;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.TestConfig;
import orlov.programming.springcoregym.service.training.TrainingTypeService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class TrainingTypeLoaderTest {

    @Autowired
    private TrainingTypeLoader trainingTypeLoader;

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Test
    void loadTrainingTypes() {
        trainingTypeLoader.loadDefaultTrainingTypes();

        assertEquals(5, trainingTypeService.findAll().size());
    }
}