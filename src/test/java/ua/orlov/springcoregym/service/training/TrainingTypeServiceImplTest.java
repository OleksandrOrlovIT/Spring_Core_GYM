package ua.orlov.springcoregym.service.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.dao.impl.training.TrainingTypeDao;
import ua.orlov.springcoregym.model.training.TrainingType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void createGivenNullThenException() {
        TrainingType trainingType = null;

        var e = assertThrows(NullPointerException.class, () -> trainingTypeService.create(trainingType));
        assertEquals("TrainingType must not be null", e.getMessage());
    }

    @Test
    void createGivenNameNullThenException() {
        TrainingType trainingType = TrainingType.builder().build();

        var e = assertThrows(NullPointerException.class, () -> trainingTypeService.create(trainingType));
        assertEquals("TrainingType name must not be null", e.getMessage());
    }

    @Test
    void createGivenValidThenSuccess() {
        TrainingType trainingType = TrainingType.builder().trainingTypeName("name").build();

        when(trainingTypeDao.create(any())).thenReturn(trainingType);

        TrainingType savedTrainingType = trainingTypeService.create(trainingType);

        assertNotNull(savedTrainingType);
    }

    @Test
    void selectGivenNullIdThenException() {
        var e = assertThrows(NullPointerException.class, () -> trainingTypeService.select(null));

        assertEquals("TrainingType id must not be null", e.getMessage());
    }

    @Test
    void selectGivenValidIdThenException() {
        when(trainingTypeDao.getById(any())).thenReturn(Optional.of(new TrainingType()));

        TrainingType trainingType = trainingTypeService.select(1L);

        assertNotNull(trainingType);
    }

    @Test
    void getAllGivenValidThenSuccess() {
        when(trainingTypeDao.getAll()).thenReturn(List.of(new TrainingType(), new TrainingType()));

        List<TrainingType> trainingTypes = trainingTypeService.getAll();

        assertNotNull(trainingTypes);
        assertEquals(2, trainingTypes.size());
    }

    @Test
    void selectGivenNotFoundThenException() {
        long id = 1L;

        when(trainingTypeDao.getById(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainingTypeService.select(id));

        assertEquals("TrainingType not found with id = " + id, e.getMessage());
    }

}