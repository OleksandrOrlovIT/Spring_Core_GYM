package ua.orlov.springcoregym.service.training;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.dao.impl.training.TrainingDao;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.training.TrainingType;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingServiceImpl;

    @Test
    void createGivenTrainingNullThenException() {
        Training training = null;

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingTraineeNullThenException() {
        Training training = Training.builder().build();

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training.trainee can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingTrainerNullThenException() {
        Training training = Training.builder()
                .trainee(new Trainee())
                .build();

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training.trainer can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingNameNullThenException() {
        Training training = Training.builder()
                .trainee(new Trainee())
                .trainer(new Trainer())
                .build();

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training.trainingName can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingTypeNullThenException() {
        Training training = Training.builder()
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("TRAINING NAME")
                .build();

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training.trainingType can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingDateNullThenException() {
        Training training = Training.builder()
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("TRAINING NAME")
                .trainingType(new TrainingType())
                .build();

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training.trainingDate can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingDurationNullThenException() {
        Training training = Training.builder()
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("TRAINING NAME")
                .trainingType(new TrainingType())
                .trainingDate(LocalDate.MIN)
                .build();

        var e = assertThrows(NullPointerException.class, () -> trainingServiceImpl.create(training));
        assertEquals("Training.trainingDuration can't be null", e.getMessage());
    }

    @Test
    void createGivenTrainingThenSuccess() {
        Training training = Training.builder()
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("TRAINING NAME")
                .trainingType(new TrainingType())
                .trainingDate(LocalDate.MIN)
                .trainingDuration(10L)
                .build();

        when(trainingDao.create(any())).thenReturn(training);

        assertEquals(training, trainingServiceImpl.create(training));
        verify(trainingDao, times(1)).create(any());
    }

    @Test
    void selectGivenTrainingThenSuccess() {
        Training training = new Training();

        when(trainingDao.getById(any())).thenReturn(Optional.of(training));

        Assertions.assertEquals(training, trainingServiceImpl.select(1L));
        verify(trainingDao, times(1)).getById(any());
    }

    @Test
    void selectGivenNotFoundTrainingThenFail() {
        Training training = new Training();

        when(trainingDao.getById(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainingServiceImpl.select(training.getId()));

        assertEquals("Training not found with id = " + training.getId(), e.getMessage());
        verify(trainingDao, times(1)).getById(any());
    }

    @Test
    void getAllGivenValidThenSuccess() {
        when(trainingDao.getAll()).thenReturn(List.of(new Training(), new Training()));

        List<Training> trainings = trainingServiceImpl.getAll();

        assertNotNull(trainings);
        assertEquals(2, trainings.size());
        verify(trainingDao, times(1)).getAll();
    }
}
