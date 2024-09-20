//package orlov.programming.springcoregym.dao.impl.training;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
//import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
//import orlov.programming.springcoregym.model.training.Training;
//import orlov.programming.springcoregym.model.user.Trainee;
//import orlov.programming.springcoregym.model.user.Trainer;
//import orlov.programming.springcoregym.storage.Storage;
//
//import java.util.HashMap;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainingDaoImplTest {
//    private static final String TRAINING_NULL_ERROR = "Training can't be null";
//    private static final String TRAINING_NOT_FOUND_ERROR_MESSAGE = "Training is not found for ";
//    private static final String TRAINEE_NOT_FOUND_ERROR_MESSAGE = "Trainee is not found for ";
//    private static final String TRAINER_NOT_FOUND_ERROR_MESSAGE = "Trainer is not found for ";
//
//    @Mock
//    private TraineeDao traineeDAO;
//
//    @Mock
//    private TrainerDao trainerDAO;
//
//    @Mock
//    private Storage storage;
//
//    @InjectMocks
//    private TrainingDaoImpl trainingDaoImpl;
//
//    @BeforeEach
//    void setUp() {
//        when(storage.getStorage(Training.class)).thenReturn(new HashMap<>());
//        when(storage.getNextId(Training.class)).thenReturn(1L);
//        trainingDaoImpl = new TrainingDaoImpl(storage, traineeDAO, trainerDAO);
//    }
//
//    @Test
//    void givenNull_whenCreate_thenException(){
//        var e = assertThrows(NullPointerException.class, () -> trainingDaoImpl.create(null));
//        assertEquals(TRAINING_NULL_ERROR, e.getMessage());
//    }
//
//    @Test
//    void givenNotFoundTrainee_whenCreate_thenException(){
//        Training training = new Training();
//
//        when(traineeDAO.findByObject(any())).thenReturn(Optional.empty());
//
//        var e = assertThrows(IllegalArgumentException.class, () -> trainingDaoImpl.create(training));
//        assertEquals(e.getMessage(), TRAINEE_NOT_FOUND_ERROR_MESSAGE + training.getTrainee());
//        verify(traineeDAO, times(1)).findByObject(any());
//    }
//
//    @Test
//    void givenNotFoundTrainer_whenCreate_thenException(){
//        Trainee trainee = new Trainee();
//        Training training = Training.builder().trainee(trainee).build();
//
//        when(traineeDAO.findByObject(any())).thenReturn(Optional.of(trainee));
//        when(trainerDAO.findByObject(any())).thenReturn(Optional.empty());
//
//        var e = assertThrows(IllegalArgumentException.class, () -> trainingDaoImpl.create(training));
//        assertEquals(e.getMessage(), TRAINER_NOT_FOUND_ERROR_MESSAGE + training.getTrainer());
//        verify(traineeDAO, times(1)).findByObject(any());
//        verify(trainerDAO, times(1)).findByObject(any());
//    }
//
//    @Test
//    void givenValid_whenCreate_thenSuccess(){
//        Trainee trainee = new Trainee();
//        Trainer trainer = new Trainer();
//        Training training = Training.builder().trainee(trainee).trainer(trainer).build();
//
//        when(traineeDAO.findByObject(any())).thenReturn(Optional.of(trainee));
//        when(trainerDAO.findByObject(any())).thenReturn(Optional.of(trainer));
//
//        Training result = trainingDaoImpl.create(training);
//        assertNotNull(result);
//        assertEquals(trainee, result.getTrainee());
//        assertEquals(trainer, result.getTrainer());
//        verify(traineeDAO, times(1)).findByObject(any());
//        verify(trainerDAO, times(1)).findByObject(any());
//    }
//
//    @Test
//    void given2Trainings_whenFindAll_thenSuccess(){
//        Trainee trainee = new Trainee();
//        Trainer trainer = new Trainer();
//        Training training1 = Training.builder().trainee(trainee).trainer(trainer).trainingName("1").build();
//        Training training2 = Training.builder().trainee(trainee).trainer(trainer).trainingName("2").build();
//
//        when(traineeDAO.findByObject(any())).thenReturn(Optional.of(trainee));
//        when(trainerDAO.findByObject(any())).thenReturn(Optional.of(trainer));
//
//        trainingDaoImpl.create(training1);
//        trainingDaoImpl.create(training2);
//
//        assertEquals(2, trainingDaoImpl.findAll().size());
//        verify(traineeDAO, times(2)).findByObject(any());
//        verify(trainerDAO, times(2)).findByObject(any());
//    }
//
//    @Test
//    void givenNull_whenFindByObject_thenException(){
//        var e = assertThrows(NullPointerException.class, () -> trainingDaoImpl.findByObject(null));
//        assertEquals(TRAINING_NULL_ERROR, e.getMessage());
//    }
//
//    @Test
//    void givenTraining_findByObject_thenException(){
//        Trainee trainee = new Trainee();
//        Trainer trainer = new Trainer();
//        Training training = Training.builder().trainee(trainee).trainer(trainer).trainingName("1").build();
//
//        when(traineeDAO.findByObject(any())).thenReturn(Optional.of(trainee));
//        when(trainerDAO.findByObject(any())).thenReturn(Optional.of(trainer));
//
//        trainingDaoImpl.create(training);
//
//        Training searchedTraining = new Training();
//
//        var e = assertThrows(IllegalArgumentException.class, () -> trainingDaoImpl.findByObject(searchedTraining));
//        assertEquals(TRAINING_NOT_FOUND_ERROR_MESSAGE + searchedTraining, e.getMessage());
//        verify(traineeDAO, times(1)).findByObject(any());
//        verify(trainerDAO, times(1)).findByObject(any());
//    }
//
//    @Test
//    void givenTraining_findByObject_thenSuccess(){
//        Trainee trainee = new Trainee();
//        Trainer trainer = new Trainer();
//        Training training = Training.builder().trainee(trainee).trainer(trainer).trainingName("1").build();
//
//        when(traineeDAO.findByObject(any())).thenReturn(Optional.of(trainee));
//        when(trainerDAO.findByObject(any())).thenReturn(Optional.of(trainer));
//
//        training = trainingDaoImpl.create(training);
//
//        assertEquals(Optional.of(training), trainingDaoImpl.findByObject(training));
//        verify(traineeDAO, times(1)).findByObject(any());
//        verify(trainerDAO, times(1)).findByObject(any());
//    }
//}
