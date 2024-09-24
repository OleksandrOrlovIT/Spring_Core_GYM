//package orlov.programming.springcoregym.service.training;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import orlov.programming.springcoregym.dao.Dao;
//import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
//import orlov.programming.springcoregym.model.training.Training;
//
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainingServiceImplTest {
//
//    @Mock
//    private TrainingDao trainingDao;
//
//    @InjectMocks
//    private TrainingServiceImpl trainingServiceImpl;
//
//    @Test
//    void givenTraining_whenCreate_thenSuccess(){
//        Training training = new Training();
//
//        when(trainingDao.create(any())).thenReturn(training);
//
//        assertEquals(training, trainingServiceImpl.create(training));
//        verify(trainingDao, times(1)).create(any());
//    }
//
////    @Test
////    void givenTraining_whenSelect_thenSuccess(){
////        Training training = new Training();
////
////        when(trainingDao.findByObject(any())).thenReturn(Optional.of(training));
////
////        assertEquals(training, trainingServiceImpl.select(training));
////        verify(trainingDao, times(1)).findByObject(any());
////    }
////
////    @Test
////    void givenNotFoundTraining_whenSelect_thenFail(){
////        Training training = new Training();
////
////        when(trainingDao.findByObject(any())).thenReturn(Optional.empty());
////
////        var e = assertThrows(NoSuchElementException.class, () -> trainingServiceImpl.select(training));
////
////        assertEquals("Training not found " + training, e.getMessage());
////        verify(trainingDao, times(1)).findByObject(any());
////    }
//}
