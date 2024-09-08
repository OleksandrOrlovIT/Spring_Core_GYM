package orlov.programming.spring_core_gym.service.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.training.Training;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private DAO<Training> trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void givenTraining_whenCreate_thenSuccess(){
        Training training = new Training();

        when(trainingDAO.create(any())).thenReturn(training);

        assertEquals(training, trainingService.create(training));
    }

    @Test
    void givenTraining_whenSelect_thenSuccess(){
        Training training = new Training();

        when(trainingDAO.findByObject(any())).thenReturn(training);

        assertEquals(training, trainingService.select(training));
    }
}