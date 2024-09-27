package orlov.programming.springcoregym.service.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.user.trainee.TraineeServiceImpl;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    private static final Long ID = 1L;
    private static final String USERNAME = "USERNAME";
    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String PASSWORD = "1111111111";

    @Mock
    private TraineeDao traineeDAO;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private TrainerDao trainerDAO;

    @InjectMocks
    private TraineeServiceImpl traineeServiceImpl;

    @Test
    void whenDelete_thenSuccess(){
        assertDoesNotThrow(() -> traineeServiceImpl.deleteByUsername(USERNAME));
    }

    @Test
    void givenNull_whenUpdate_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenFirstNameNull_whenUpdate_thenException(){
        Trainee trainee = new Trainee();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(trainee));
        assertEquals("Trainee's firstName can't be null", e.getMessage());
    }

    @Test
    void givenLastNameNull_whenUpdate_thenException(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(trainee));
        assertEquals("Trainee's lastName can't be null", e.getMessage());
    }

    @Test
    void givenNotFound_whenUpdate_thenException(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> traineeServiceImpl.update(trainee));
        assertEquals("Trainee not found with id = " + trainee.getId(), e.getMessage());
        verify(traineeDAO, times(1)).findById(any());
    }

    @Test
    void givenValid_whenUpdate_thenSuccess(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).password(PASSWORD).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(PASSWORD, resultTrainee.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());

        verify(traineeDAO, times(1)).findById(any());
        verify(passwordGenerator, times(1)).generatePassword();
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenValidWithWrongPassword_whenUpdate_thenSuccess(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(PASSWORD, resultTrainee.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(traineeDAO, times(1)).findById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenValidWithNewPassword_whenUpdate_thenSuccess(){
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(password2, resultTrainee.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainee.getUsername());
        verify(traineeDAO, times(1)).findById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenValidWithNewPasswordAndOldUserName_whenUpdate_thenSetsOldName(){
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(traineeDAO.findByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainee.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build()));
        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(password2, resultTrainee.getPassword());
        assertEquals(USERNAME, resultTrainee.getUsername());

        verify(traineeDAO, times(1)).findByUsername(any());
        verify(traineeDAO, times(1)).findById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenNull_whenCreate_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenFirstNameNull_whenCreate_thenException(){
        Trainee trainee = new Trainee();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(trainee));
        assertEquals("Trainee's firstName can't be null", e.getMessage());
    }

    @Test
    void givenLastNameNull_whenCreate_thenException(){
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(trainee));
        assertEquals("Trainee's lastName can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenSuccess(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.findByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainee.builder().id(ID + 1).username(FIRST_NAME + "." + LAST_NAME).build()));
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeServiceImpl.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(PASSWORD, trainee.getPassword());
        assertEquals(46, trainee.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(traineeDAO, times(1)).findByUsername(any());
        verify(traineeDAO, times(1)).create(any());
    }

    @Test
    void givenPasswordDifferentLengthAndNewName_whenCreate_thenSuccess(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).password(PASSWORD + "!").build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeServiceImpl.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(PASSWORD, trainee.getPassword());
        assertEquals(10, trainee.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(traineeDAO, times(1)).findByUsername(any());
        verify(traineeDAO, times(1)).create(any());
    }

    @Test
    void givenPassword_whenCreate_thenSuccess(){
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).password(password2).build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();

        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeServiceImpl.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(password2, trainee.getPassword());
        assertEquals(10, trainee.getUsername().length());

        verify(traineeDAO, times(1)).findByUsername(any());
        verify(traineeDAO, times(1)).create(any());
    }

    @Test
    void givenNull_whenSelect_thenException(){
        var e = assertThrows(NoSuchElementException.class, () -> traineeServiceImpl.select(null));
        assertEquals("Trainee not found with id = " + null, e.getMessage());
    }

    @Test
    void givenValid_whenSelect_thenSuccess(){
        Trainee trainee = new Trainee();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));

        Trainee foundTrainee = traineeServiceImpl.select(trainee.getId());

        assertEquals(trainee, foundTrainee);
        verify(traineeDAO, times(1)).findById(any());
    }

    @Test
    void givenDifferentIsActive_whenUpdate_thenException(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(false).build();
        Trainee trainee2 = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(true).build();

        when(traineeDAO.findById(anyLong())).thenReturn(Optional.of(trainee));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.update(trainee2));
        assertEquals("IsActive field can't be changed in update", e.getMessage());

        verify(traineeDAO, times(1)).findById(any());
        verify(passwordGenerator, times(1)).generatePassword();
    }

    @Test
    void givenNotFound_whenUserNameMatchPassword_thenException(){
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.userNameMatchPassword("", ""));

        assertEquals("Trainee not found ", e.getMessage());
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenUserNameMatchPassword_thenSuccess(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));

        boolean result = traineeServiceImpl.userNameMatchPassword(trainee.getUsername(), trainee.getPassword());

        assertTrue(result);
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenNullPassword_whenUserNameMatchPassword_thenFalse(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(false).build();

        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));

        boolean result = traineeServiceImpl.userNameMatchPassword(trainee.getUsername(), trainee.getPassword());

        assertFalse(result);
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenWrongPassword_whenUserNameMatchPassword_thenException(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee traineeWithWrongPassword =
                Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(false).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.changePassword(traineeWithWrongPassword, "newPass"));

        assertEquals("Wrong password for trainee " + traineeWithWrongPassword.getUsername(), e.getMessage());
        verify(traineeDAO, times(1)).findById(any());
    }

    @Test
    void givenValidPassword_whenUserNameMatchPassword_thenSuccess(){
        String newPassword = "newPass";
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee traineeWithWrongPassword =
                Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee traineeWithNewPassword =
                Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(newPassword).isActive(false).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(trainee);

        Trainee updatedTrainee = traineeServiceImpl.changePassword(traineeWithWrongPassword, newPassword);

        assertEquals(traineeWithNewPassword, updatedTrainee);
        assertEquals(newPassword, updatedTrainee.getPassword());
        verify(traineeDAO, times(1)).findById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenActivatedTrainee_whenActivateTrainee_thenException(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.activateTrainee(trainee.getId()));

        assertEquals("Trainee is already active " + trainee, e.getMessage());
        verify(traineeDAO, times(1)).findById(any());
    }

    @Test
    void givenNonActivatedTrainee_whenActivateTrainee_thenSuccess(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee trainee2 = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(trainee2);

        Trainee updatedTrainee = traineeServiceImpl.activateTrainee(trainee.getId());
        assertEquals(trainee2, updatedTrainee);
        verify(traineeDAO, times(1)).findById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenDeActivatedTrainee_whenDeactivateTrainee_thenException(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.deactivateTrainee(trainee.getId()));

        assertEquals("Trainee is already deactivated " + trainee, e.getMessage());
        verify(traineeDAO, times(1)).findById(any());
    }

    @Test
    void givenActivatedTrainee_whenDeactivateTrainee_thenSuccess(){
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainee trainee2 = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(trainee2);

        Trainee updatedTrainee = traineeServiceImpl.deactivateTrainee(trainee.getId());
        assertEquals(trainee2, updatedTrainee);
        verify(traineeDAO, times(1)).findById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void givenValid_whenGetTrainingsByDate_thenReturnsTrainings(){
        List<Training> trainings = List.of(new Training(), new Training());

        when(traineeDAO.getTrainingsByDateUsernameTrainingType(any())).thenReturn(trainings);

        TraineeTrainingDTO traineeTrainingDTO = new TraineeTrainingDTO(LocalDate.MIN, LocalDate.MIN, FIRST_NAME,
                "TRAINING");

        List<Training> foundTrainings = traineeServiceImpl.getTrainingsByDateTraineeNameTrainingType(traineeTrainingDTO);

        assertEquals(trainings, foundTrainings);
        verify(traineeDAO, times(1)).getTrainingsByDateUsernameTrainingType(any());
    }

    @Test
    void givenValid_whenFindAll_thenSuccess(){
        when(traineeDAO.findAll()).thenReturn(List.of(new Trainee(), new Trainee()));

        List<Trainee> trainees = traineeServiceImpl.findAll();

        assertNotNull(trainees);
        assertEquals(2, trainees.size());
        verify(traineeDAO, times(1)).findAll();
    }

    @Test
    void givenNotFound_whenAuthenticateTrainee_thenException(){
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.authenticateTrainee(USERNAME, ""));

        assertEquals("Trainee not found " + USERNAME, e.getMessage());
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenWrongPassword_whenAuthenticateTrainee_thenException(){
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.ofNullable(Trainee.builder().password("pass").build()));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.authenticateTrainee(USERNAME, PASSWORD));

        assertEquals("Wrong password for trainee " + USERNAME, e.getMessage());
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenAuthenticateTrainee_thenSuccess(){
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.ofNullable(Trainee.builder().password(PASSWORD).build()));

        Trainee trainee = traineeServiceImpl.authenticateTrainee(USERNAME, PASSWORD);

        assertNotNull(trainee);
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenNotFound_whenFindByUsername_thenException(){
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.findByUsername(USERNAME));

        assertEquals("Trainee not found " + USERNAME, e.getMessage());
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenFindByUsername_thenSuccess(){
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.ofNullable(Trainee.builder().build()));

        Trainee trainee = traineeServiceImpl.findByUsername(USERNAME);

        assertNotNull(trainee);
        verify(traineeDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenNoTrainers_whenUpdateTraineeTrainers_thenException(){
        Trainee trainee = Trainee.builder().build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByIds(any())).thenReturn(List.of());

        var e = assertThrows(EntityNotFoundException.class, () -> traineeServiceImpl.updateTraineeTrainers(ID, List.of()));

        assertEquals("No trainers found with the provided IDs", e.getMessage());
        verify(traineeDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).findByIds(any());
    }

    @Test
    void givenValid_whenUpdateTraineeTrainers_thenSuccess(){
        Trainee trainee = Trainee.builder().build();
        List<Trainer> trainers = List.of(Trainer.builder().build(), Trainer.builder().trainees(List.of(trainee)).build());
        Trainee traineeWithTrainers = Trainee.builder().trainers(trainers).build();

        when(traineeDAO.findById(any())).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByIds(any())).thenReturn(trainers);
        when(traineeDAO.update(trainee)).thenReturn(traineeWithTrainers);

        traineeServiceImpl.updateTraineeTrainers(ID, List.of(1L, 2L));

        verify(traineeDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).findByIds(any());
        verify(traineeDAO, times(1)).update(any());
    }
}
