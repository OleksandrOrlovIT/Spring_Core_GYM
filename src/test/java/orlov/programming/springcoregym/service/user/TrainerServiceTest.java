package orlov.programming.springcoregym.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.user.trainer.TrainerServiceImpl;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

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
    void givenNull_whenUpdate_thenException(){
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenFirstNameNull_whenUpdate_thenException(){
        Trainer trainer = new Trainer();
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer's firstName can't be null", e.getMessage());
    }

    @Test
    void givenLastNameNull_whenUpdate_thenException(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer's lastName can't be null", e.getMessage());
    }

    @Test
    void givenNotFound_whenUpdate_thenException(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(trainerDAO.findById(any())).thenReturn(null);

        var e = assertThrows(NoSuchElementException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer not found with id = " + trainer.getId(), e.getMessage());
        verify(trainerDAO, times(1)).findById(any());
    }

    @Test
    void givenValid_whenUpdate_thenSuccess(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.findById(anyLong())).thenReturn(trainer);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());

        verify(trainerDAO, times(1)).findById(any());
        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenValidWithWrongPassword_whenUpdate_thenSuccess(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.findById(anyLong())).thenReturn(trainer);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());

        verify(trainerDAO, times(1)).findById(any());
        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenValidWithNewPassword_whenUpdate_thenSuccess(){
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(password2, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());

        verify(trainerDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenValidWithNewPasswordAndOldUserName_whenUpdate_thenSetsOldName(){
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer updatedTrainer = Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(trainerDAO.findByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder().username(USERNAME).id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build()));
        when(trainerDAO.findById(any())).thenReturn(trainer);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(password2, resultTrainer.getPassword());
        assertEquals(USERNAME, resultTrainer.getUsername());

        verify(trainerDAO, times(1)).findByUsername(any());
        verify(trainerDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenNull_whenCreate_thenException(){
        var e = assertThrows(NullPointerException.class, () -> trainerService.create(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenFirstNameNull_whenCreate_thenException(){
        Trainer trainer = new Trainer();
        var e = assertThrows(NullPointerException.class, () -> trainerService.create(trainer));
        assertEquals("Trainer's firstName can't be null", e.getMessage());
    }

    @Test
    void givenLastNameNull_whenCreate_thenException(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).build();
        var e = assertThrows(NullPointerException.class, () -> trainerService.create(trainer));
        assertEquals("Trainer's lastName can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenSuccess(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).isActive(true).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.findByUsername(any()))
                .thenReturn(Optional.ofNullable(Trainer.builder().id(ID + 1).username(FIRST_NAME + "." + LAST_NAME).build()));
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(46, trainer.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).findByUsername(any());
        verify(trainerDAO, times(1)).create(any());
    }

    @Test
    void givenPasswordDifferentLengthAndNewName_whenCreate_thenSuccess(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());

        verify(passwordGenerator, times(1)).generatePassword();
        verify(trainerDAO, times(1)).findByUsername(any());
        verify(trainerDAO, times(1)).create(any());
    }

    @Test
    void givenPassword_whenCreate_thenSuccess(){
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).isActive(true).build();

        when(trainerDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(password2, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());

        verify(trainerDAO, times(1)).findByUsername(any());
        verify(trainerDAO, times(1)).create(any());
    }

    @Test
    void givenNull_whenSelect_thenException(){
        var e = assertThrows(NoSuchElementException.class, () -> trainerService.select(null));
        assertEquals("Trainer not found with id = " + null, e.getMessage());
    }

    @Test
    void givenValid_whenSelect_thenSuccess(){
        Trainer trainer = new Trainer();

        when(trainerDAO.findById(any())).thenReturn(trainer);

        Trainer foundTrainer = trainerService.select(trainer.getId());

        assertEquals(trainer, foundTrainer);
        verify(trainerDAO, times(1)).findById(any());
    }

    @Test
    void givenDifferentIsActive_whenUpdate_thenException(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(false).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").isActive(true).build();

        when(trainerDAO.findById(anyLong())).thenReturn(trainer);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer2));
        assertEquals("IsActive field can't be changed in update", e.getMessage());

        verify(trainerDAO, times(1)).findById(any());
        verify(passwordGenerator, times(1)).generatePassword();
    }

    @Test
    void givenNotFound_whenUserNameMatchPassword_thenException(){
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.userNameMatchPassword("", ""));

        assertEquals("Trainer not found ", e.getMessage());
        verify(trainerDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenUserNameMatchPassword_thenSuccess(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDAO.findByUsername(any())).thenReturn(Optional.of(trainer));

        boolean result = trainerService.userNameMatchPassword(trainer.getUsername(), trainer.getPassword());

        assertTrue(result);
        verify(trainerDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenWrongPassword_whenUserNameMatchPassword_thenException(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithWrongPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").isActive(false).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerWithWrongPassword, "newPass"));

        assertEquals("Wrong password for trainer " + trainerWithWrongPassword.getUsername(), e.getMessage());
        verify(trainerDAO, times(1)).findById(any());
    }

    @Test
    void givenValidPassword_whenUserNameMatchPassword_thenSuccess(){
        String newPassword = "newPass";
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithWrongPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainerWithNewPassword =
                Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(newPassword).isActive(false).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);
        when(trainerDAO.update(any())).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.changePassword(trainerWithWrongPassword, newPassword);

        assertEquals(trainerWithNewPassword, updatedTrainer);
        assertEquals(newPassword, updatedTrainer.getPassword());
        verify(trainerDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenActivatedTrainer_whenActivateTrainer_thenException(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.activateTrainer(trainer.getId()));

        assertEquals("Trainer is already active " + trainer, e.getMessage());
        verify(trainerDAO, times(1)).findById(any());
    }

    @Test
    void givenNonActivatedTrainer_whenActivateTrainer_thenSuccess(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);
        when(trainerDAO.update(any())).thenReturn(trainer2);

        Trainer updatedTrainer = trainerService.activateTrainer(trainer.getId());
        assertEquals(trainer2, updatedTrainer);
        verify(trainerDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenDeActivatedTrainer_whenDeactivateTrainer_thenException(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.deactivateTrainer(trainer.getId()));

        assertEquals("Trainer is already deactivated " + trainer, e.getMessage());
        verify(trainerDAO, times(1)).findById(any());
    }

    @Test
    void givenActivatedTrainer_whenDeactivateTrainer_thenSuccess(){
        Trainer trainer = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(true).build();
        Trainer trainer2 = Trainer.builder().id(ID).firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).isActive(false).build();

        when(trainerDAO.findById(any())).thenReturn(trainer);
        when(trainerDAO.update(any())).thenReturn(trainer2);

        Trainer updatedTrainer = trainerService.deactivateTrainer(trainer.getId());
        assertEquals(trainer2, updatedTrainer);
        verify(trainerDAO, times(1)).findById(any());
        verify(trainerDAO, times(1)).update(any());
    }

    @Test
    void givenValid_whenGetTrainingsByDate_thenReturnsTrainings(){
        List<Training> trainings = List.of(new Training(), new Training());

        when(trainerDAO.getTrainingsByDateAndUsername(any(), any(), any())).thenReturn(trainings);

        List<Training> foundTrainings = trainerService.getTrainingsByDate(LocalDate.MIN, LocalDate.MIN, FIRST_NAME);

        assertEquals(trainings, foundTrainings);
        verify(trainerDAO, times(1)).getTrainingsByDateAndUsername(any(), any(), any());
    }

    @Test
    void givenNotFound_whenGetTrainersWithoutPassedTrainee_thenException(){
        when(traineeDao.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.getTrainersWithoutPassedTrainee(FIRST_NAME));

        assertEquals("Trainee not found " + FIRST_NAME, e.getMessage());
        verify(traineeDao, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenGetTrainersWithoutPassedTrainee_thenSuccess(){
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(traineeDao.findByUsername(any())).thenReturn(Optional.of(new Trainee()));
        when(trainerDAO.getTrainersWithoutPassedTrainee(any())).thenReturn(trainers);

        List<Trainer> foundTrainers = trainerService.getTrainersWithoutPassedTrainee(FIRST_NAME);
        assertEquals(trainers, foundTrainers);
        verify(traineeDao, times(1)).findByUsername(any());
        verify(trainerDAO, times(1)).getTrainersWithoutPassedTrainee(any());
    }

    @Test
    void givenValid_whenFindAll_thenSuccess(){
        when(trainerDAO.findAll()).thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> trainers = trainerService.findAll();

        assertNotNull(trainers);
        assertEquals(2, trainers.size());
        verify(trainerDAO, times(1)).findAll();
    }

    @Test
    void givenNotFound_whenAuthenticateTrainer_thenException(){
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.authenticateTrainer(USERNAME, ""));

        assertEquals("Trainer not found " + USERNAME, e.getMessage());
        verify(trainerDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenWrongPassword_whenAuthenticateTrainer_thenException(){
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().password("pass").build()));

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.authenticateTrainer(USERNAME, PASSWORD));

        assertEquals("Wrong password for trainer " + USERNAME, e.getMessage());
        verify(trainerDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenAuthenticateTrainer_thenSuccess(){
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().password(PASSWORD).build()));

        Trainer trainer = trainerService.authenticateTrainer(USERNAME, PASSWORD);

        assertNotNull(trainer);
        verify(trainerDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenNotFound_whenFindByUsername_thenException(){
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.empty());

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.findByUsername(USERNAME));

        assertEquals("Trainer not found " + USERNAME, e.getMessage());
        verify(trainerDAO, times(1)).findByUsername(any());
    }

    @Test
    void givenValid_whenFindByUsername_thenSuccess(){
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.ofNullable(Trainer.builder().build()));

        Trainer trainer = trainerService.findByUsername(USERNAME);

        assertNotNull(trainer);
        verify(trainerDAO, times(1)).findByUsername(any());
    }
}
