package ua.orlov.springcoregym.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.dao.impl.user.trainee.TraineeDao;
import ua.orlov.springcoregym.dao.impl.user.trainer.TrainerDao;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.service.user.trainer.TrainerServiceImpl;
import ua.orlov.springcoregym.util.PasswordGenerator;
import ua.orlov.springcoregym.util.model.Pageable;

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
    private TrainerDao trainerDAO;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private TraineeDao traineeDao;

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
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer's lastName can't be null", e.getMessage());
    }

    @Test
    void updateGivenNotFoundThenException() {
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.empty());

        var e = assertThrows(NoSuchElementException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer not found with id = " + trainer.getId(), e.getMessage());
        verify(trainerDAO, times(1)).getById(any());
    }

    @Test
    void updateGivenValidThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.getById(anyLong())).thenReturn(Optional.of(trainer));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());

        verify(trainerDAO, times(1)).getById(any());
        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithWrongPasswordThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.getById(anyLong())).thenReturn(Optional.of(trainer));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());

        verify(trainerDAO, times(1)).getById(any());
        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithNewPasswordThenSuccess() {
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(password2, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());

        verify(trainerDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithNewPasswordAndOldUserNameThenSetsOldName() {
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(trainerDAO.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build()));
        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(password2, resultTrainer.getPassword());
        assertEquals(USERNAME, resultTrainer.getUsername());

        verify(trainerDAO, times(1)).getByUsername(any());
        verify(trainerDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void updateGivenValidWithPasswordSameLengthThenOldPassword() {
        String updatedPassword = "2222222222";
        Trainer oldTrainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainer inputtedTrainer = Trainer.builder().username(USERNAME).id(ID)
                .firstName(FIRST_NAME).lastName(LAST_NAME).password(updatedPassword).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).id(ID)
                .firstName(FIRST_NAME).lastName(LAST_NAME).password(updatedPassword).isActive(true).build();

        when(trainerDAO.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder()
                        .username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME)
                        .password(updatedPassword).isActive(true).build()));
        when(trainerDAO.getById(any())).thenReturn(Optional.of(oldTrainer));
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);
        when(passwordGenerator.getPasswordLength()).thenReturn(updatedPassword.length());

        Trainer resultTrainer = trainerService.update(inputtedTrainer);
        assertEquals(updatedPassword, resultTrainer.getPassword());
        assertEquals(USERNAME, resultTrainer.getUsername());

        verify(trainerDAO, times(1)).getByUsername(any());
        verify(trainerDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).update(any());
        verify(passwordGenerator, times(1)).getPasswordLength();
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

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.getByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder().id(ID + 1).username(FIRST_NAME + "." + LAST_NAME).build()));
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(46, trainer.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).getByUsername(any());
        verify(trainerDAO, times(1)).create(any());
    }

    @Test
    void createGivenPasswordDifferentLengthAndNewNameThenSuccess() {
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).getByUsername(any());
        verify(trainerDAO, times(1)).create(any());
    }

    @Test
    void createGivenPasswordThenSuccess() {
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(trainerDAO.getByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.create(any())).thenReturn(createdTrainer);
        when(passwordGenerator.getPasswordLength()).thenReturn(10);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(password2, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());

        verify(trainerDAO, times(1)).getByUsername(any());
        verify(trainerDAO, times(1)).create(any());
        verify(passwordGenerator, times(1)).getPasswordLength();
    }

    @Test
    void selectGivenNullThenException() {
        var e = assertThrows(NoSuchElementException.class, () -> trainerService.select(null));
        assertEquals("Trainer not found with id = " + null, e.getMessage());
    }

    @Test
    void selectGivenValidThenSuccess() {
        Trainer trainer = new Trainer();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));

        Trainer foundTrainer = trainerService.select(trainer.getId());

        assertEquals(trainer, foundTrainer);
        verify(trainerDAO, times(1)).getById(any());
    }

    @Test
    void updateGivenDifferentIsActiveThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(false).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(true).build();

        when(trainerDAO.getById(anyLong())).thenReturn(Optional.of(trainer));
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer2));
        assertEquals("IsActive field can't be changed in update", e.getMessage());

        verify(trainerDAO, times(1)).getById(any());
        verify(passwordGenerator, times(1)).generatePassword();
    }

    @Test
    void isUserNameMatchPasswordGivenNotFoundThenException() {
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.isUserNameMatchPassword("", ""));

        assertEquals("Trainer not found ", e.getMessage());
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenValidThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDAO.getByUsername(any())).thenReturn(Optional.of(trainer));

        boolean result = trainerService.isUserNameMatchPassword(trainer.getUsername(), trainer.getPassword());

        assertTrue(result);
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenNullPasswordThenFalse() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(false).build();

        when(trainerDAO.getByUsername(any())).thenReturn(Optional.of(trainer));

        boolean result = trainerService.isUserNameMatchPassword(trainer.getUsername(), trainer.getPassword());

        assertFalse(result);
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void isUserNameMatchPasswordGivenWrongPasswordThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithWrongPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(false).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerWithWrongPassword, "newPass"));

        assertEquals("Wrong password for trainer " + trainerWithWrongPassword.getUsername(), e.getMessage());
        verify(trainerDAO, times(1)).getById(any());
    }

    @Test
    void isUserNameMatchPasswordGivenValidPasswordThenSuccess() {
        String newPassword = "newPass";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithWrongPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithNewPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(newPassword).isActive(false).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any())).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.changePassword(trainerWithWrongPassword, newPassword);

        assertEquals(trainerWithNewPassword, updatedTrainer);
        assertEquals(newPassword, updatedTrainer.getPassword());
        verify(trainerDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void activateTrainerGivenActivatedTrainerThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.activateTrainer(trainer.getId()));

        assertEquals("Trainer is already active " + trainer, e.getMessage());
        verify(trainerDAO, times(1)).getById(any());
    }

    @Test
    void activateTrainerGivenNonActivatedTrainerThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any())).thenReturn(trainer2);

        Trainer updatedTrainer = trainerService.activateTrainer(trainer.getId());
        assertEquals(trainer2, updatedTrainer);
        verify(trainerDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void deactivateTrainerGivenDeactivatedTrainerThenException() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.deactivateTrainer(trainer.getId()));

        assertEquals("Trainer is already deactivated " + trainer, e.getMessage());
        verify(trainerDAO, times(1)).getById(any());
    }

    @Test
    void deactivateTrainerGivenActivatedTrainerThenSuccess() {
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDAO.getById(any())).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any())).thenReturn(trainer2);

        Trainer updatedTrainer = trainerService.deactivateTrainer(trainer.getId());
        assertEquals(trainer2, updatedTrainer);
        verify(trainerDAO, times(1)).getById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void getTrainingsByDateGivenValidThenReturnsTrainings() {
        List<Training> trainings = List.of(new Training(), new Training());

        when(trainerDAO.getTrainingsByDateAndUsername(any(), any(), any())).thenReturn(trainings);

        List<Training> foundTrainings = trainerService.getTrainingsByDate(LocalDate.MIN, LocalDate.MIN, FIRST_NAME);

        assertEquals(trainings, foundTrainings);
        verify(trainerDAO, times(1)).getTrainingsByDateAndUsername(any(), any(), any());
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
        when(trainerDAO.getTrainersWithoutPassedTrainee(any(), any())).thenReturn(trainers);

        List<Trainer> foundTrainers = trainerService.getTrainersWithoutPassedTrainee(FIRST_NAME, new Pageable(0, 2));
        assertEquals(trainers, foundTrainers);
        verify(traineeDao, times(1)).getByUsername(any());
        verify(trainerDAO, times(1)).getTrainersWithoutPassedTrainee(any(), any());
    }

    @Test
    void getAllGivenValidThenSuccess() {
        when(trainerDAO.getAll()).thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> trainers = trainerService.getAll();

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        verify(trainerDAO, times(1)).getAll();
    }

    @Test
    void authenticateTrainerGivenNotFoundThenException() {
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.authenticateTrainer(USERNAME, ""));

        assertEquals("Trainer not found " + USERNAME, e.getMessage());
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void authenticateTrainerGivenWrongPasswordThenException() {
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().password("pass").build()));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.authenticateTrainer(USERNAME, PASSWORD));

        assertEquals("Wrong password for trainer " + USERNAME, e.getMessage());
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void authenticateTrainerGivenValidThenSuccess() {
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().password(PASSWORD).build()));

        Trainer trainer = trainerService.authenticateTrainer(USERNAME, PASSWORD);

        assertNotNull(trainer);
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void getByUsernameGivenNotFoundThenException() {
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.getByUsername(USERNAME));

        assertEquals("Trainer not found " + USERNAME, e.getMessage());
        verify(trainerDAO, times(1)).getByUsername(any());
    }

    @Test
    void getByUsernameGivenValidThenSuccess() {
        when(trainerDAO.getByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().build()));

        Trainer trainer = trainerService.getByUsername(USERNAME);

        assertNotNull(trainer);
        verify(trainerDAO, times(1)).getByUsername(any());
    }
}
