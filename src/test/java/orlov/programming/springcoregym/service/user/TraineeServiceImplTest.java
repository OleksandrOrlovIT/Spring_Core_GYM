//package orlov.programming.springcoregym.service.user;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import orlov.programming.springcoregym.dao.DaoUsernameFindable;
//import orlov.programming.springcoregym.model.user.Trainee;
//import orlov.programming.springcoregym.service.user.trainee.TraineeServiceImpl;
//import orlov.programming.springcoregym.util.PasswordGenerator;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TraineeServiceImplTest {
//
//    private static final String FIRST_NAME = "FIRST";
//    private static final String LAST_NAME = "LAST";
//    private static final String PASSWORD = "1111111111";
//
//    @Mock
//    private DaoUsernameFindable<Trainee> traineeDAO;
//
//    @Mock
//    private PasswordGenerator passwordGenerator;
//
//    @InjectMocks
//    private TraineeServiceImpl traineeServiceImpl;
//
//    @Test
//    void whenDelete_thenSuccess(){
//        assertDoesNotThrow(() -> traineeServiceImpl.delete(new Trainee()));
//    }
//
//    @Test
//    void givenNull_whenUpdate_thenException(){
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(null));
//        assertEquals("Trainee can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenFirstNameNull_whenUpdate_thenException(){
//        Trainee trainee = new Trainee();
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(trainee));
//        assertEquals("Trainee's firstName can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenLastNameNull_whenUpdate_thenException(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(trainee));
//        assertEquals("Trainee's lastName can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenNotFound_whenUpdate_thenException(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
//
//        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());
//
//        var e = assertThrows(NoSuchElementException.class, () -> traineeServiceImpl.update(trainee));
//        assertEquals("Trainee not found " + trainee, e.getMessage());
//        verify(traineeDAO, times(1)).findByUsername(any());
//    }
//
//    @Test
//    void givenValid_whenUpdate_thenSuccess(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
//        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();
//
//        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));
//        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
//        when(traineeDAO.update(any())).thenReturn(updatedTrainee);
//
//        Trainee resultTrainee = traineeServiceImpl.update(trainee);
//        assertEquals(PASSWORD, resultTrainee.getPassword());
//        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
//
//        verify(traineeDAO, times(1)).findByUsername(any());
//        verify(passwordGenerator, times(1)).generatePassword();
//        verify(traineeDAO, times(1)).update(any());
//    }
//
//    @Test
//    void givenValidWithWrongPassword_whenUpdate_thenSuccess(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").build();
//        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();
//
//        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));
//        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
//        when(traineeDAO.update(any())).thenReturn(updatedTrainee);
//
//        Trainee resultTrainee = traineeServiceImpl.update(trainee);
//        assertEquals(PASSWORD, resultTrainee.getPassword());
//        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
//
//        verify(passwordGenerator, times(1)).generatePassword();
//        verify(traineeDAO, times(1)).findByUsername(any());
//        verify(traineeDAO, times(1)).update(any());
//    }
//
//    @Test
//    void givenValidWithNewPassword_whenUpdate_thenSuccess(){
//        String password2 = "2222222222";
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
//        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
//
//        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));
//        when(traineeDAO.update(any())).thenReturn(updatedTrainee);
//
//        Trainee resultTrainee = traineeServiceImpl.update(trainee);
//        assertEquals(password2, resultTrainee.getPassword());
//        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
//        verify(traineeDAO, times(1)).findByUsername(any());
//        verify(traineeDAO, times(1)).update(any());
//    }
//
//    @Test
//    void givenNull_whenCreate_thenException(){
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(null));
//        assertEquals("Trainee can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenFirstNameNull_whenCreate_thenException(){
//        Trainee trainee = new Trainee();
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(trainee));
//        assertEquals("Trainee's firstName can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenLastNameNull_whenCreate_thenException(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(trainee));
//        assertEquals("Trainee's lastName can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenValid_whenCreate_thenSuccess(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
//        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
//
//        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
//        when(traineeDAO.findAll()).thenReturn(List.of(Trainee.builder().username(FIRST_NAME + "." + LAST_NAME).build()));
//        when(traineeDAO.create(any())).thenReturn(createdTrainee);
//
//        traineeServiceImpl.create(trainee);
//        assertNotEquals(createdTrainee, trainee);
//        assertEquals(PASSWORD, trainee.getPassword());
//        assertEquals(50, trainee.getUsername().length());
//
//        verify(passwordGenerator, times(1)).generatePassword();
//        verify(traineeDAO, times(1)).findAll();
//        verify(traineeDAO, times(1)).create(any());
//    }
//
//    @Test
//    void givenPasswordDifferentLengthAndNewName_whenCreate_thenSuccess(){
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").build();
//        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();
//
//        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
//        when(traineeDAO.findAll()).thenReturn(List.of(Trainee.builder().username("name").build()));
//        when(traineeDAO.create(any())).thenReturn(createdTrainee);
//
//        traineeServiceImpl.create(trainee);
//        assertNotEquals(createdTrainee, trainee);
//        assertEquals(PASSWORD, trainee.getPassword());
//        assertEquals(10, trainee.getUsername().length());
//
//        verify(passwordGenerator, times(1)).generatePassword();
//        verify(traineeDAO, times(1)).findAll();
//        verify(traineeDAO, times(1)).create(any());
//    }
//
//    @Test
//    void givenPassword_whenCreate_thenSuccess(){
//        String password2 = "2222222222";
//        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
//        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
//
//        when(traineeDAO.findAll()).thenReturn(List.of(Trainee.builder().username("name").build()));
//        when(traineeDAO.create(any())).thenReturn(createdTrainee);
//
//        traineeServiceImpl.create(trainee);
//        assertNotEquals(createdTrainee, trainee);
//        assertEquals(password2, trainee.getPassword());
//        assertEquals(10, trainee.getUsername().length());
//
//        verify(traineeDAO, times(1)).findAll();
//        verify(traineeDAO, times(1)).create(any());
//    }
//
//    @Test
//    void givenNull_whenSelect_thenException(){
//        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.select(null));
//        assertEquals("Trainee can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenValid_whenSelect_thenSuccess(){
//        Trainee trainee = new Trainee();
//
//        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));
//
//        Trainee foundTrainee = traineeServiceImpl.select(trainee);
//
//        assertEquals(trainee, foundTrainee);
//        verify(traineeDAO, times(1)).findByUsername(any());
//    }
//}
