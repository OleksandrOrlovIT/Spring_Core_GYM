package ua.orlov.springcoregym.service.user.trainer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.dao.impl.user.UserDao;
import ua.orlov.springcoregym.dao.impl.user.trainee.TraineeDao;
import ua.orlov.springcoregym.dao.impl.user.trainer.TrainerDao;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.model.user.User;
import ua.orlov.springcoregym.service.password.PasswordService;
import ua.orlov.springcoregym.model.page.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    private static final Long ID = 1L;
    private static final String USERNAME = "USERNAME";
    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String PASSWORD = "1111111111";

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void updateGivenNullThenException() {
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void updateGivenFirstNameNullThenException() {
        Trainer trainer = new Trainer();
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer's firstName can't be null", e.getMessage());
    }

    @Test
    void updateGivenLastNameNullThenException() {
        Trainer trainer = Trainer.builder().username(USERNAME).firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer's lastName can't be null", e.getMessage());
    }

    @Test
    void updateGivenNotFoundThenException() {
        Trainer trainer = Trainer.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer not found " + trainer.getUsername(), e.getMessage());
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void updateGivenValidThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD).isActive(true).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.of(trainer));
        when(passwordService.generatePassword()).thenReturn(PASSWORD);
        when(trainerDao.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(USERNAME, trainer.getUsername());

        verify(trainerDao, times(2)).getByUsername(any());
        verify(passwordService, times(1)).generatePassword();
        verify(trainerDao, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithWrongPasswordThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD + "1").isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD).isActive(true).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.of(trainer));
        when(passwordService.generatePassword()).thenReturn(PASSWORD);
        when(trainerDao.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(USERNAME, trainer.getUsername());

        verify(trainerDao, times(2)).getByUsername(any());
        verify(passwordService, times(1)).generatePassword();
        verify(trainerDao, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithNewPasswordThenSuccess() {
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(updatedTrainer);
        when(passwordService.getPasswordLength()).thenReturn(password2.length() + 1);

        Trainer resultTrainer = trainerService.update(trainer);
        assertNotEquals(password2, resultTrainer.getPassword());
        assertEquals(USERNAME, trainer.getUsername());

        verify(trainerDao, times(2)).getByUsername(any());
        verify(trainerDao, times(1)).update(any());
        verify(passwordService, times(1)).generatePassword();
    }

    @Test
    void updateGivenValidWithNewPasswordAndOldUserNameThenSetsOldName() {
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(password2).isActive(true).build();

        when(trainerDao.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build()));
        when(trainerDao.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(password2, resultTrainer.getPassword());
        assertEquals(USERNAME, resultTrainer.getUsername());

        verify(trainerDao, times(2)).getByUsername(any());
        verify(trainerDao, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithPasswordSameLengthThenOldPassword() {
        String updatedPassword = "2222222222";
        Trainer oldTrainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainer inputtedTrainer = Trainer.builder().username(USERNAME).id(ID)
                .firstName(FIRST_NAME).lastName(LAST_NAME).password(updatedPassword).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).id(ID)
                .firstName(FIRST_NAME).lastName(LAST_NAME).password(updatedPassword).isActive(true).build();

        when(trainerDao.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder()
                        .username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                        .password(updatedPassword).isActive(true).build()));
        when(trainerDao.update(any())).thenReturn(updatedTrainer);
        when(passwordService.getPasswordLength()).thenReturn(updatedPassword.length());

        Trainer resultTrainer = trainerService.update(inputtedTrainer);
        assertEquals(updatedPassword, resultTrainer.getPassword());
        assertEquals(USERNAME, resultTrainer.getUsername());

        verify(trainerDao, times(2)).getByUsername(any());
        verify(trainerDao, times(1)).update(any());
        verify(passwordService, times(1)).getPasswordLength();
    }

    @Test
    void createGivenNullThenException() {
        var e = assertThrows(NullPointerException.class, () -> trainerService.create(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void createGivenFirstNameNullThenException() {
        Trainer trainer = new Trainer();
        var e = assertThrows(NullPointerException.class, () -> trainerService.create(trainer));
        assertEquals("Trainer's firstName can't be null", e.getMessage());
    }

    @Test
    void createGivenLastNameNullThenException() {
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> trainerService.create(trainer));
        assertEquals("Trainer's lastName can't be null", e.getMessage());
    }

    @Test
    void createGivenValidThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Optional<Trainer> foundTrainer =
                Optional.ofNullable(Trainer.builder().id(ID + 1).username(FIRST_NAME + "." + LAST_NAME).build());

        when(passwordService.generatePassword()).thenReturn(PASSWORD);
        when(userDao.getByUsername(any()))
                .thenReturn(Optional.of(User.builder().id(ID + 1).username(FIRST_NAME + "." + LAST_NAME).build()));
        when(trainerDao.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(46, trainer.getUsername().length());

        verify(passwordService, times(1)).generatePassword();
        verify(userDao, times(1)).getByUsername(any());
        verify(trainerDao, times(1)).create(any());
    }

    @Test
    void createGivenPasswordDifferentLengthAndNewNameThenSuccess() {
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(passwordService.generatePassword()).thenReturn(PASSWORD);
        when(userDao.getByUsername(any())).thenReturn(Optional.empty());
        when(trainerDao.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());

        verify(passwordService, times(1)).generatePassword();
        verify(userDao, times(1)).getByUsername(any());
        verify(trainerDao, times(1)).create(any());
    }

    @Test
    void createGivenPasswordThenSuccess() {
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(userDao.getByUsername(any())).thenReturn(Optional.empty());
        when(trainerDao.create(any())).thenReturn(createdTrainer);
        when(passwordService.getPasswordLength()).thenReturn(10);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(password2, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());

        verify(userDao, times(1)).getByUsername(any());
        verify(trainerDao, times(1)).create(any());
        verify(passwordService, times(1)).getPasswordLength();
    }

    @Test
    void selectGivenNullThenException() {
        var e = assertThrows(NoSuchElementException.class, () -> trainerService.select(null));
        assertEquals("Trainer not found with id = " + null, e.getMessage());
    }

    @Test
    void selectGivenValidThenSuccess() {
        Trainer trainer = new Trainer();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));

        Trainer foundTrainer = trainerService.select(trainer.getId());

        assertEquals(trainer, foundTrainer);
        verify(trainerDao, times(1)).getById(any());
    }

    @Test
    void updateGivenDifferentIsActiveThenException() {
        Trainer trainer = Trainer.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD + "1").isActive(false).build();
        Trainer trainer2 = Trainer.builder().id(ID).username(USERNAME).firstName(FIRST_NAME).lastName(LAST_NAME)
                .password(PASSWORD + "1").isActive(true).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.of(trainer));
        when(passwordService.generatePassword()).thenReturn(PASSWORD);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer2));
        assertEquals("IsActive field can't be changed in update", e.getMessage());

        verify(trainerDao, times(1)).getByUsername(any());
        verify(passwordService, times(1)).generatePassword();
    }

    @Test
    void isUserNameMatchPasswordGivenNotFoundThenException() {
        when(trainerDao.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.isUserNameMatchPassword("", ""));

        assertEquals("Trainer not found ", e.getMessage());
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenValidThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.of(trainer));

        boolean result = trainerService.isUserNameMatchPassword(trainer.getUsername(), trainer.getPassword());

        assertTrue(result);
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenNullPasswordThenFalse() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(false).build();

        when(trainerDao.getByUsername(any())).thenReturn(Optional.of(trainer));

        boolean result = trainerService.isUserNameMatchPassword(trainer.getUsername(), trainer.getPassword());

        assertFalse(result);
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenWrongPasswordThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithWrongPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(false).build();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerWithWrongPassword, "newPass"));

        assertEquals("Wrong password for trainer " + trainerWithWrongPassword.getUsername(), e.getMessage());
        verify(trainerDao, times(1)).getById(any());
    }

    @Test
    void isUserNameMatchPasswordGivenValidPasswordThenSuccess() {
        String newPassword = "newPass";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithWrongPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithNewPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(newPassword).isActive(false).build();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.changePassword(trainerWithWrongPassword, newPassword);

        assertEquals(trainerWithNewPassword, updatedTrainer);
        assertEquals(newPassword, updatedTrainer.getPassword());
        verify(trainerDao, times(1)).getById(any());
        verify(trainerDao, times(1)).update(any());
    }

    @Test
    void activateTrainerGivenActivatedTrainerThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.activateTrainer(trainer.getId()));

        assertEquals("Trainer is already active " + trainer, e.getMessage());
        verify(trainerDao, times(1)).getById(any());
    }

    @Test
    void activateTrainerGivenNonActivatedTrainerThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer2);

        Trainer updatedTrainer = trainerService.activateTrainer(trainer.getId());
        assertEquals(trainer2, updatedTrainer);
        verify(trainerDao, times(1)).getById(any());
        verify(trainerDao, times(1)).update(any());
    }

    @Test
    void deactivateTrainerGivenDeactivatedTrainerThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.deactivateTrainer(trainer.getId()));

        assertEquals("Trainer is already deactivated " + trainer, e.getMessage());
        verify(trainerDao, times(1)).getById(any());
    }

    @Test
    void deactivateTrainerGivenActivatedTrainerThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDao.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer2);

        Trainer updatedTrainer = trainerService.deactivateTrainer(trainer.getId());
        assertEquals(trainer2, updatedTrainer);
        verify(trainerDao, times(1)).getById(any());
        verify(trainerDao, times(1)).update(any());
    }

    @Test
    void getTrainingsByDateGivenValidThenReturnsTrainings() {
        List<Training> trainings = List.of(new Training(), new Training());

        when(trainerDao.getTrainingsByDateAndUsername(any(), any(), any())).thenReturn(trainings);

        List<Training> foundTrainings = trainerService.getTrainingsByDate(LocalDate.MIN, LocalDate.MIN, FIRST_NAME);

        assertEquals(trainings, foundTrainings);
        verify(trainerDao, times(1)).getTrainingsByDateAndUsername(any(), any(), any());
    }

    @Test
    void getTrainersWithoutPassedTraineeGivenNotFoundThenException() {
        when(traineeDao.getByUsername(any())).thenReturn(Optional.empty());

        Pageable pageable = new Pageable(0, 2);
        var e = assertThrows(IllegalArgumentException.class,
                () -> trainerService.getTrainersWithoutPassedTrainee(FIRST_NAME, pageable));

        assertEquals("Trainee not found " + FIRST_NAME, e.getMessage());
        verify(traineeDao, times(1)).getByUsername(any());
    }

    @Test
    void getTrainersWithoutPassedTraineeGivenValidThenSuccess() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(traineeDao.getByUsername(any())).thenReturn(Optional.of(new Trainee()));
        when(trainerDao.getTrainersWithoutPassedTrainee(any(), any())).thenReturn(trainers);

        List<Trainer> foundTrainers = trainerService.getTrainersWithoutPassedTrainee(FIRST_NAME, new Pageable(0, 2));
        assertEquals(trainers, foundTrainers);
        verify(traineeDao, times(1)).getByUsername(any());
        verify(trainerDao, times(1)).getTrainersWithoutPassedTrainee(any(), any());
    }

    @Test
    void getAllGivenValidThenSuccess() {
        when(trainerDao.getAll()).thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> trainers = trainerService.getAll();

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        verify(trainerDao, times(1)).getAll();
    }

    @Test
    void authenticateTrainerGivenNotFoundThenException() {
        when(trainerDao.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.authenticateTrainer(USERNAME, ""));

        assertEquals("Trainer not found " + USERNAME, e.getMessage());
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void authenticateTrainerGivenWrongPasswordThenException() {
        when(trainerDao.getByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().password("pass").build()));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.authenticateTrainer(USERNAME, PASSWORD));

        assertEquals("Wrong password for trainer " + USERNAME, e.getMessage());
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void authenticateTrainerGivenValidThenSuccess() {
        when(trainerDao.getByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().password(PASSWORD).build()));

        Trainer trainer = trainerService.authenticateTrainer(USERNAME, PASSWORD);

        assertNotNull(trainer);
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void getByUsernameGivenNotFoundThenException() {
        when(trainerDao.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainerService.getByUsername(USERNAME));

        assertEquals("Trainer not found " + USERNAME, e.getMessage());
        verify(trainerDao, times(1)).getByUsername(any());
    }

    @Test
    void getByUsernameGivenValidThenSuccess() {
        when(trainerDao.getByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().build()));

        Trainer trainer = trainerService.getByUsername(USERNAME);

        assertNotNull(trainer);
        verify(trainerDao, times(1)).getByUsername(any());
    }
}
