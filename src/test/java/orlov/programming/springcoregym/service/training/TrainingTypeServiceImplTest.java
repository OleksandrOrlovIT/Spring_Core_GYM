package orlov.programming.springcoregym.service.training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.dao.impl.training.TrainingTypeDao;
import orlov.programming.springcoregym.model.training.TrainingType;

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
    void givenNull_whenCreate_thenException(){
        TrainingType trainingType = null;

        var e = assertThrows(NullPointerException.class, () -> trainingTypeService.create(trainingType));
        assertEquals("TrainingType must not be null", e.getMessage());
    }

    @Test
    void givenNameNull_whenCreate_thenException(){
        TrainingType trainingType = TrainingType.builder().build();

        var e = assertThrows(NullPointerException.class, () -> trainingTypeService.create(trainingType));
        assertEquals("TrainingType name must not be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenSuccess(){
        TrainingType trainingType = TrainingType.builder().trainingTypeName("name").build();

        when(trainingTypeDao.create(any())).thenReturn(trainingType);

        TrainingType savedTrainingType = trainingTypeService.create(trainingType);

        assertNotNull(savedTrainingType);
    }

    @Test
    void givenNullId_whenSelect_thenException(){
        var e = assertThrows(NullPointerException.class, () -> trainingTypeService.select(null));

        assertEquals("TrainingType id must not be null", e.getMessage());
    }

    @Test
    void givenValidId_whenSelect_thenException(){
        when(trainingTypeDao.findById(any())).thenReturn(Optional.of(new TrainingType()));

        TrainingType trainingType = trainingTypeService.select(1L);

        assertNotNull(trainingType);
    }

    @Test
    void givenValid_whenFindAll_thenSuccess(){
        when(trainingTypeDao.findAll()).thenReturn(List.of(new TrainingType(), new TrainingType()));

        List<TrainingType> trainingTypes = trainingTypeService.findAll();

        assertNotNull(trainingTypes);
        assertEquals(2, trainingTypes.size());
    }

    @Test
    void givenNotFound_whenSelect_thenException(){
        long id = 1L;

        when(trainingTypeDao.findById(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainingTypeService.select(id));

        assertEquals("TrainingType not found with id = " + id, e.getMessage());
    }

}