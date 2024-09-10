package orlov.programming.springcoregym.dao.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.storage.Storage;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDAOTest {

    @Mock
    private Storage storage;

    @InjectMocks
    private TrainerDAO trainerDAO;

    @BeforeEach
    void setUp() {
        when(storage.getStorage(Trainer.class)).thenReturn(new HashMap<>());
        when(storage.getNextId(Trainer.class)).thenReturn(1L);
        trainerDAO = new TrainerDAO(storage);
    }

    @Test
    void givenNull_whenCreate_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> trainerDAO.create(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenTrainerWithId_whenCreate_thenThrowsException() {
        var e = assertThrows(IllegalArgumentException.class, () -> trainerDAO.create(Trainer.builder().userId(1L).build()));
        assertEquals("Entity's id has to be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenCreate() {
        Trainer trainer = new Trainer();

        Trainer savedTrainer = trainerDAO.create(trainer);

        assertNotNull(savedTrainer);
        assertEquals(1, savedTrainer.getUserId());
        assertEquals(1, trainerDAO.findAll().size());
    }

    @Test
    void givenNull_whenUpdate_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> trainerDAO.update(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenTrainerWithNullId_whenUpdate_thenThrowsException() {
        Trainer trainer = new Trainer();
        var e = assertThrows(IllegalArgumentException.class, () -> trainerDAO.update(trainer));
        assertEquals("Trainer's id can't be null", e.getMessage());
    }

    @Test
    void givenNonExistingTrainer_whenUpdate_thenThrowsException() {
        Trainer trainer = Trainer.builder().userId(1L).build();
        var e = assertThrows(IllegalArgumentException.class, () -> trainerDAO.update(trainer));
        assertEquals("Entity does not exist", e.getMessage());
    }

    @Test
    void givenValid_whenUpdate_thenUpdate() {
        Trainer trainer = trainerDAO.create(new Trainer());

        String userName = "UserName";
        Trainer updatedTrainer = trainerDAO.update(Trainer.builder().userId(1L).username(userName).build());

        assertNotNull(updatedTrainer);
        assertEquals(trainer.getUserId(), updatedTrainer.getUserId());
        assertNotEquals(trainer.getUsername(), updatedTrainer.getUsername());
        assertEquals(userName, updatedTrainer.getUsername());
    }

    @Test
    void givenNull_whenDelete_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> trainerDAO.delete(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenTrainerWithNullId_whenDelete_thenThrowsException() {
        Trainer trainer = new Trainer();
        var e = assertThrows(IllegalArgumentException.class, () -> trainerDAO.delete(trainer));
        assertEquals("Trainer's id can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenDelete_thenDelete() {
        trainerDAO.create(new Trainer());
        Trainer trainer2 = trainerDAO.create(new Trainer());

        trainerDAO.delete(trainer2);

        assertEquals(1, trainerDAO.findAll().size());
    }

    @Test
    void given2Entities_whenFindAll_thenFindAll() {
        trainerDAO.create(new Trainer());
        trainerDAO.create(new Trainer());

        assertEquals(2, trainerDAO.findAll().size());
    }

    @Test
    void givenNull_whenFindByObject_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> trainerDAO.findByObject(null));
        assertEquals("Trainer can't be null", e.getMessage());
    }

    @Test
    void givenTrainerWithNullId_whenfindByObject_thenThrowsException() {
        Trainer trainer = new Trainer();
        var e = assertThrows(IllegalArgumentException.class, () -> trainerDAO.findByObject(trainer));
        assertEquals("Trainer's id can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenFindByObject_thenFindByObject() {
        Trainer trainer = trainerDAO.create(new Trainer());

        assertEquals(Optional.of(trainer), trainerDAO.findByObject(trainer));
    }

    @Test
    void givenNull_whenFindByUsername_thenException(){
        var e = assertThrows(NullPointerException.class, () -> trainerDAO.findByUsername(null));
        assertEquals("Username can't be null", e.getMessage());
    }

    @Test
    void given0Entity_whenFindByUsername_thenNull(){
        trainerDAO.create(new Trainer());
        assertEquals(Optional.empty(), trainerDAO.findByUsername("someUserName"));
    }

    @Test
    void givenEntity_whenFindByUsername_thenFound(){
        String username = "UserName";
        Trainer trainer = Trainer.builder().username(username).build();

        trainer = trainerDAO.create(trainer);

        assertEquals(Optional.of(trainer), trainerDAO.findByUsername(username));
    }
}
