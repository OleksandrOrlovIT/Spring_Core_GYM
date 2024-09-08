package orlov.programming.spring_core_gym.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.spring_core_gym.dao.impl.user.DAOUsernameFindable;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.util.PasswordGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String PASSWORD = "1111111111";

    @Mock
    private DAOUsernameFindable<Trainer> trainerDAO;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TrainerService trainerService;

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

        when(trainerDAO.findByUsername(any())).thenReturn(null);

        var e = assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer));
        assertEquals("Trainer not found for " + trainer, e.getMessage());
    }

    @Test
    void givenValid_whenUpdate_thenSuccess(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(trainerDAO.findByUsername(any())).thenReturn(trainer);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());
    }

    @Test
    void givenValidWithWrongPassword_whenUpdate_thenSuccess(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "1").build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(trainerDAO.findByUsername(any())).thenReturn(trainer);
        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(PASSWORD, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());
    }

    @Test
    void givenValidWithNewPassword_whenUpdate_thenSuccess(){
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
        Trainer updatedTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();

        when(trainerDAO.findByUsername(any())).thenReturn(trainer);
        when(trainerDAO.update(any())).thenReturn(updatedTrainer);

        Trainer resultTrainer = trainerService.update(trainer);
        assertEquals(password2, resultTrainer.getPassword());
        assertEquals(FIRST_NAME + "." + LAST_NAME, trainer.getUsername());
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
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.findAll()).thenReturn(List.of(Trainer.builder().username(FIRST_NAME + "." + LAST_NAME).build()));
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(50, trainer.getUsername().length());
    }

    @Test
    void givenPasswordDifferentLengthAndNewName_whenCreate_thenSuccess(){
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD + "!").build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(PASSWORD).build();

        when(passwordGenerator.generatePassword()).thenReturn(PASSWORD);
        when(trainerDAO.findAll()).thenReturn(List.of(Trainer.builder().username("name").build()));
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(PASSWORD, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());
    }

    @Test
    void givenPassword_whenCreate_thenSuccess(){
        String password2 = "2222222222";
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();
        Trainer createdTrainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).password(password2).build();

        when(trainerDAO.findAll()).thenReturn(List.of(Trainer.builder().username("name").build()));
        when(trainerDAO.create(any())).thenReturn(createdTrainer);

        trainerService.create(trainer);
        assertNotEquals(createdTrainer, trainer);
        assertEquals(password2, trainer.getPassword());
        assertEquals(10, trainer.getUsername().length());
    }

    @Test
    void givenNull_whenSelect_thenException(){
        var e = assertThrows(NullPointerException.class, () -> trainerService.select(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenSelect_thenSuccess(){
        Trainer trainer = new Trainer();

        when(trainerDAO.findByUsername(any())).thenReturn(trainer);

        Trainer foundTrainer = trainerService.select(trainer);

        assertEquals(trainer, foundTrainer);
    }
}