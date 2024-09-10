package orlov.programming.springcoregym.service.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.dao.DAO;
import orlov.programming.springcoregym.model.training.Training;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        verify(trainingDAO, times(1)).create(any());
    }

    @Test
    void givenTraining_whenSelect_thenSuccess(){
        Training training = new Training();

        when(trainingDAO.findByObject(any())).thenReturn(Optional.of(training));

        assertEquals(training, trainingService.select(training));
        verify(trainingDAO, times(1)).findByObject(any());
    }

    @Test
    void givenNotFoundTraining_whenSelect_thenFail(){
        Training training = new Training();

        when(trainingDAO.findByObject(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainingService.select(training));

        assertEquals("Training not found " + training, e.getMessage());
        verify(trainingDAO, times(1)).findByObject(any());
    }
}
