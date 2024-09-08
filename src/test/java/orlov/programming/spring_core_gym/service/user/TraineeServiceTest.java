package orlov.programming.spring_core_gym.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.spring_core_gym.dao.impl.user.DAOUsernameFindable;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.util.PasswordGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String PASSWORD = "1111111111";

    @Mock
    private DAOUsernameFindable<Trainee> traineeDAO;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void whenDelete_thenSuccess(){
        assertDoesNotThrow(() -> traineeService.delete(new Trainee()));
    }

    @Test
    void givenNull_whenUpdate_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeService.update(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenFirstNameNull_whenUpdate_thenException(){
        Trainee trainee = new Trainee();
        var e = assertThrows(NullPointerException.class, () -> traineeService.update(trainee));
        assertEquals("Trainee's firstName can't be null", e.getMessage());
    }

    @Test
    void givenLastNameNull_whenUpdate_thenException(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> traineeService.update(trainee));
        assertEquals("Trainee's lastName can't be null", e.getMessage());
    }

    @Test
    void givenNotFound_whenUpdate_thenException(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(traineeDAO.findByUsername(any())).thenReturn(null);

        var e = assertThrows(IllegalArgumentException.class, () -> traineeService.update(trainee));
        assertEquals("Trainee not found for " + trainee, e.getMessage());
    }

    @Test
    void givenValid_whenUpdate_thenSuccess(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(traineeDAO.findByUsername(any())).thenReturn(trainee);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeService.update(trainee);
        assertEquals(PASSWORD, resultTrainee.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
    }

    @Test
    void givenValidWithWrongPassword_whenUpdate_thenSuccess(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").build();
        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(traineeDAO.findByUsername(any())).thenReturn(trainee);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeService.update(trainee);
        assertEquals(PASSWORD, resultTrainee.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
    }

    @Test
    void givenValidWithNewPassword_whenUpdate_thenSuccess(){
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();

        when(traineeDAO.findByUsername(any())).thenReturn(trainee);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeService.update(trainee);
        assertEquals(password2, resultTrainee.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
    }

    @Test
    void givenNull_whenCreate_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeService.create(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenFirstNameNull_whenCreate_thenException(){
        Trainee trainee = new Trainee();
        var e = assertThrows(NullPointerException.class, () -> traineeService.create(trainee));
        assertEquals("Trainee's firstName can't be null", e.getMessage());
    }

    @Test
    void givenLastNameNull_whenCreate_thenException(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> traineeService.create(trainee));
        assertEquals("Trainee's lastName can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenSuccess(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.findAll()).thenReturn(List.of(Trainee.builder().username(FIRST_NAME + "." + LAST_NAME).build()));
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeService.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(PASSWORD, trainee.getPassword());
        assertEquals(50, trainee.getUsername().length());
    }

    @Test
    void givenPasswordDifferentLengthAndNewName_whenCreate_thenSuccess(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.findAll()).thenReturn(List.of(Trainee.builder().username("name").build()));
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeService.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(PASSWORD, trainee.getPassword());
        assertEquals(10, trainee.getUsername().length());
    }

    @Test
    void givenPassword_whenCreate_thenSuccess(){
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();

        when(traineeDAO.findAll()).thenReturn(List.of(Trainee.builder().username("name").build()));
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeService.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(password2, trainee.getPassword());
        assertEquals(10, trainee.getUsername().length());
    }

    @Test
    void givenNull_whenSelect_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeService.select(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenSelect_thenSuccess(){
        Trainee trainee = new Trainee();

        when(traineeDAO.findByUsername(any())).thenReturn(trainee);

        Trainee foundTrainee = traineeService.select(trainee);

        assertEquals(trainee, foundTrainee);
    }
}