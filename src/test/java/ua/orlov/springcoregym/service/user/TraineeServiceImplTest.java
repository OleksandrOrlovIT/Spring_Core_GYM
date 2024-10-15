package ua.orlov.springcoregym.service.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.dao.impl.user.UserDao;
import ua.orlov.springcoregym.dao.impl.user.trainee.TraineeDao;
import ua.orlov.springcoregym.dao.impl.user.trainer.TrainerDao;
import ua.orlov.springcoregym.dto.trainee.TraineeTrainingDTO;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.service.user.trainee.TraineeServiceImpl;
import ua.orlov.springcoregym.util.PasswordGenerator;

import java.time.LocalDate;
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

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TraineeServiceImpl traineeServiceImpl;

    @Test
    void deleteThenSuccess() {
        assertDoesNotThrow(() -> traineeServiceImpl.deleteByUsername(USERNAME));
    }

    @Test
    void updateGivenNullThenException() {
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void updateGivenFirstNameNullThenException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(trainee));
        assertEquals("Trainee's firstName can't be null", e.getMessage());
    }

    @Test
    void updateGivenLastNameNullThenException() {
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.update(trainee));
        assertEquals("Trainee's lastName can't be null", e.getMessage());
    }

    @Test
    void updateGivenNotFoundThenException() {
        Trainee trainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> traineeServiceImpl.update(trainee));
        assertEquals("Trainee not found " + trainee.getUsername(), e.getMessage());
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void updateGivenValidThenSuccess() {
        Trainee trainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .isActive(true).password(PASSWORD).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(trainee));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(PASSWORD, resultTrainee.getPassword());
        assertEquals(USERNAME, trainee.getUsername());

        verify(traineeDAO, times(2)).getByUsername(any());
        verify(passwordGenerator, times(1)).generatePassword();
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithWrongPasswordThenSuccess() {
        Trainee trainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD + "1").isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD).isActive(true).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(trainee));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(PASSWORD, resultTrainee.getPassword());
        assertEquals(USERNAME, trainee.getUsername());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(traineeDAO, times(2)).getByUsername(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithNewPasswordThenSuccess() {
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);
        when(passwordGenerator.getPasswordLength()).thenReturn(password2.length());

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(password2, resultTrainee.getPassword());
        assertEquals(USERNAME, trainee.getUsername());

        verify(traineeDAO, times(2)).getByUsername(any());
        verify(traineeDAO, times(1)).update(any());
        verify(passwordGenerator, times(1)).getPasswordLength();
    }

    @Test
    void updateGivenValidWithNewPasswordAndOldUserNameThenSetsOldName() {
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();

        when(traineeDAO.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainee.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build()));
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);

        Trainee resultTrainee = traineeServiceImpl.update(trainee);
        assertEquals(password2, resultTrainee.getPassword());
        assertEquals(USERNAME, resultTrainee.getUsername());

        verify(traineeDAO, times(2)).getByUsername(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithPasswordSameLengthThenOldPassword() {
        String updatedPassword = "2222222222";
        Trainee oldTrainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD).isActive(true).build();
        Trainee inputtedTrainee = Trainee.builder().username(USERNAME).id(ID)
                .firstName(FIRST_NAME).lastName(LAST_NAME).password(updatedPassword).isActive(true).build();
        Trainee updatedTrainee = Trainee.builder().username(USERNAME).id(ID)
                .firstName(FIRST_NAME).lastName(LAST_NAME).password(updatedPassword).isActive(true).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(oldTrainee));
        when(traineeDAO.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainee.builder()
                        .username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                        .password(updatedPassword).isActive(true).build()));
        when(traineeDAO.update(any())).thenReturn(updatedTrainee);
        when(passwordGenerator.getPasswordLength()).thenReturn(updatedPassword.length());

        Trainee resultTrainee = traineeServiceImpl.update(inputtedTrainee);
        assertEquals(updatedPassword, resultTrainee.getPassword());
        assertEquals(USERNAME, resultTrainee.getUsername());

        verify(traineeDAO, times(2)).getByUsername(any());
        verify(traineeDAO, times(1)).update(any());
        verify(passwordGenerator, times(1)).getPasswordLength();
    }

    @Test
    void createGivenNullThenException() {
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void createGivenFirstNameNullThenException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(trainee));
        assertEquals("Trainee's firstName can't be null", e.getMessage());
    }

    @Test
    void createGivenLastNameNullThenException() {
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> traineeServiceImpl.create(trainee));
        assertEquals("Trainee's lastName can't be null", e.getMessage());
    }

    @Test
    void createGivenValidThenSuccess() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(userDao.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainee.builder().id(ID + 1).username(FIRST_NAME + "." + LAST_NAME).build()));
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeServiceImpl.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(PASSWORD, trainee.getPassword());
        assertEquals(46, trainee.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(userDao, times(1)).getByUsername(any());
        verify(traineeDAO, times(1)).create(any());
    }

    @Test
    void createGivenPasswordDifferentLengthAndNewNameThenSuccess() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).password(PASSWORD + "!").build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(userDao.getByUsername(any())).thenReturn(Optional.empty());
        when(traineeDAO.create(any())).thenReturn(createdTrainee);

        traineeServiceImpl.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(PASSWORD, trainee.getPassword());
        assertEquals(10, trainee.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(userDao, times(1)).getByUsername(any());
        verify(traineeDAO, times(1)).create(any());
    }

    @Test
    void createGivenPasswordThenSuccess() {
        String password2 = "2222222222";
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).password(password2).build();
        Trainee createdTrainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();

        when(userDao.getByUsername(any())).thenReturn(Optional.empty());
        when(traineeDAO.create(any())).thenReturn(createdTrainee);
        when(passwordGenerator.getPasswordLength()).thenReturn(10);

        traineeServiceImpl.create(trainee);
        assertNotEquals(createdTrainee, trainee);
        assertEquals(password2, trainee.getPassword());
        assertEquals(10, trainee.getUsername().length());

        verify(userDao, times(1)).getByUsername(any());
        verify(traineeDAO, times(1)).create(any());
        verify(passwordGenerator, times(1)).getPasswordLength();
    }

    @Test
    void selectGivenNullThenException() {
        var e = assertThrows(NoSuchElementException.class, () -> traineeServiceImpl.select(null));
        assertEquals("Trainee not found with id = " + null, e.getMessage());
    }

    @Test
    void selectGivenValidThenSuccess() {
        Trainee trainee = new Trainee();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));

        Trainee foundTrainee = traineeServiceImpl.select(trainee.getId());

        assertEquals(trainee, foundTrainee);
        verify(traineeDAO, times(1)).getById(any());
    }

    @Test
    void updateGivenDifferentIsActiveThenException() {
        Trainee trainee = Trainee.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD + "1").isActive(false).build();
        Trainee trainee2 = Trainee.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD + "1").isActive(true).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(trainee));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.update(trainee2));
        assertEquals("IsActive field can't be changed in update", e.getMessage());

        verify(traineeDAO, times(1)).getByUsername(any());
        verify(passwordGenerator, times(1)).generatePassword();
    }

    @Test
    void isUserNameMatchPasswordGivenNotFoundThenException() {
        when(traineeDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.isUserNameMatchPassword("", ""));

        assertEquals("Trainee not found ", e.getMessage());
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenValidThenSuccess() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(trainee));

        boolean result = traineeServiceImpl.isUserNameMatchPassword(trainee.getUsername(), trainee.getPassword());

        assertTrue(result);
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenNullPasswordThenFalse() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(false).build();

        when(traineeDAO.getByUsername(any())).thenReturn(Optional.of(trainee));

        boolean result = traineeServiceImpl.isUserNameMatchPassword(trainee.getUsername(), trainee.getPassword());

        assertFalse(result);
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenWrongPasswordThenException() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee traineeWithWrongPassword =
                Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(false).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.changePassword(traineeWithWrongPassword, "newPass"));

        assertEquals("Wrong password for trainee " + traineeWithWrongPassword.getUsername(), e.getMessage());
        verify(traineeDAO, times(1)).getById(any());
    }

    @Test
    void isUserNameMatchPasswordGivenValidPasswordThenSuccess() {
        String newPassword = "newPass";
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee traineeWithWrongPassword =
                Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee traineeWithNewPassword =
                Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(newPassword).isActive(false).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(trainee);

        Trainee updatedTrainee = traineeServiceImpl.changePassword(traineeWithWrongPassword, newPassword);

        assertEquals(traineeWithNewPassword, updatedTrainee);
        assertEquals(newPassword, updatedTrainee.getPassword());
        verify(traineeDAO, times(1)).getById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void activateTraineeGivenActivatedTraineeThenException() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.activateTrainee(trainee.getId()));

        assertEquals("Trainee is already active " + trainee, e.getMessage());
        verify(traineeDAO, times(1)).getById(any());
    }

    @Test
    void activateTraineeGivenNonActivatedTraineeThenSuccess() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainee trainee2 = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(trainee2);

        Trainee updatedTrainee = traineeServiceImpl.activateTrainee(trainee.getId());
        assertEquals(trainee2, updatedTrainee);
        verify(traineeDAO, times(1)).getById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void deactivateTraineeGivenDeActivatedTraineeThenException() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.deactivateTrainee(trainee.getId()));

        assertEquals("Trainee is already deactivated " + trainee, e.getMessage());
        verify(traineeDAO, times(1)).getById(any());
    }

    @Test
    void deactivateTraineeGivenActivatedTraineeThenSuccess() {
        Trainee trainee = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainee trainee2 = Trainee.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(trainee2);

        Trainee updatedTrainee = traineeServiceImpl.deactivateTrainee(trainee.getId());
        assertEquals(trainee2, updatedTrainee);
        verify(traineeDAO, times(1)).getById(any());
        verify(traineeDAO, times(1)).update(any());
    }

    @Test
    void getTrainingsByDateGivenValidThenReturnsTrainings() {
        List<Training> trainings = List.of(new Training(), new Training());

        when(traineeDAO.getTrainingsByTraineeTrainingDTO(any())).thenReturn(trainings);

        TraineeTrainingDTO traineeTrainingDTO = new TraineeTrainingDTO(LocalDate.MIN, LocalDate.MIN, FIRST_NAME,
                "TRAINING");

        List<Training> foundTrainings = traineeServiceImpl.getTrainingsByTraineeTrainingDTO(traineeTrainingDTO);

        assertEquals(trainings, foundTrainings);
        verify(traineeDAO, times(1)).getTrainingsByTraineeTrainingDTO(any());
    }

    @Test
    void getAllGivenValidThenSuccess() {
        when(traineeDAO.getAll()).thenReturn(List.of(new Trainee(), new Trainee()));

        List<Trainee> trainees = traineeServiceImpl.getAll();

        assertNotNull(trainees);
        assertEquals(2, trainees.size());
        verify(traineeDAO, times(1)).getAll();
    }

    @Test
    void authenticateTraineeGivenNotFoundThenException() {
        when(traineeDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.authenticateTrainee(USERNAME, ""));

        assertEquals("Trainee not found " + USERNAME, e.getMessage());
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void authenticateTraineeGivenWrongPasswordThenException() {
        when(traineeDAO.getByUsername(any())).thenReturn(Optional.ofNullable(Trainee.builder().password("pass").build()));

        var e = assertThrows(IllegalArgumentException.class, () -> traineeServiceImpl.authenticateTrainee(USERNAME, PASSWORD));

        assertEquals("Wrong password for trainee " + USERNAME, e.getMessage());
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void authenticateTraineeGivenValidThenSuccess() {
        when(traineeDAO.getByUsername(any())).thenReturn(Optional.ofNullable(Trainee.builder().password(PASSWORD).build()));

        Trainee trainee = traineeServiceImpl.authenticateTrainee(USERNAME, PASSWORD);

        assertNotNull(trainee);
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void getByUsernameGivenNotFoundThenException() {
        when(traineeDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> traineeServiceImpl.getByUsername(USERNAME));

        assertEquals("Trainee not found " + USERNAME, e.getMessage());
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void getByUsernameGivenValidThenSuccess() {
        when(traineeDAO.getByUsername(any())).thenReturn(Optional.ofNullable(Trainee.builder().build()));

        Trainee trainee = traineeServiceImpl.getByUsername(USERNAME);

        assertNotNull(trainee);
        verify(traineeDAO, times(1)).getByUsername(any());
    }

    @Test
    void updateTraineeTrainersGivenNoTrainersThenException() {
        Trainee trainee = Trainee.builder().build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));
        when(trainerDAO.getByIds(any())).thenReturn(List.of());

        var e = assertThrows(EntityNotFoundException.class, () -> traineeServiceImpl.updateTraineeTrainers(ID, List.of()));

        assertEquals("No trainers found with the provided IDs", e.getMessage());
        verify(traineeDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).getByIds(any());
    }

    @Test
    void updateTraineeTrainersGivenValidThenSuccess() {
        Trainee trainee = Trainee.builder().build();
        List<Trainer> trainers = List.of(Trainer.builder().build(), Trainer.builder().trainees(List.of(trainee)).build());
        Trainee traineeWithTrainers = Trainee.builder().trainers(trainers).build();

        when(traineeDAO.getById(any())).thenReturn(Optional.of(trainee));
        when(trainerDAO.getByIds(any())).thenReturn(trainers);
        when(traineeDAO.update(trainee)).thenReturn(traineeWithTrainers);

        traineeServiceImpl.updateTraineeTrainers(ID, List.of(1L, 2L));

        verify(traineeDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).getByIds(any());
        verify(traineeDAO, times(1)).update(any());
    }
}