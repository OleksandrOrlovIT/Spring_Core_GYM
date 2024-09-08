package orlov.programming.spring_core_gym.dao.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.storage.impl.TraineeStorage;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDAOTest {

    @Mock
    private TraineeStorage storage;

    @InjectMocks
    private TraineeDAO traineeDAO;

    @BeforeEach
    void setUp() {
        when(storage.getStorage()).thenReturn(new HashMap<>());
        when(storage.getLastKey()).thenReturn(1L);
        traineeDAO = new TraineeDAO(storage);
    }

    @Test
    void givenNull_whenCreate_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDAO.create(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithId_whenCreate_thenThrowsException() {
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDAO.create(Trainee.builder().userId(1L).build()));
        assertEquals("Trainee's id has to be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenCreate() {
        Trainee trainee = new Trainee();

        Trainee savedTrainee = traineeDAO.create(trainee);

        assertNotNull(savedTrainee);
        assertEquals(1, savedTrainee.getUserId());
        assertEquals(1, traineeDAO.findAll().size());
    }

    @Test
    void givenNull_whenUpdate_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDAO.update(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithNullId_whenUpdate_thenThrowsException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDAO.update(trainee));
        assertEquals("Trainee's id can't be null", e.getMessage());
    }

    @Test
    void givenNonExistingTrainee_whenUpdate_thenThrowsException() {
        Trainee trainee = Trainee.builder().userId(1L).build();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDAO.update(trainee));
        assertEquals("Trainee does not exist", e.getMessage());
    }

    @Test
    void givenValid_whenUpdate_thenUpdate() {
        Trainee trainee = traineeDAO.create(new Trainee());

        String userName = "UserName";
        Trainee updatedTrainee = traineeDAO.update(Trainee.builder().userId(1L).username(userName).build());

        assertNotNull(updatedTrainee);
        assertEquals(trainee.getUserId(), updatedTrainee.getUserId());
        assertNotEquals(trainee.getUsername(), updatedTrainee.getUsername());
        assertEquals(userName, updatedTrainee.getUsername());
    }

    @Test
    void givenNull_whenDelete_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDAO.delete(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithNullId_whenDelete_thenThrowsException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDAO.delete(trainee));
        assertEquals("Trainee's id can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenDelete_thenDelete() {
        traineeDAO.create(new Trainee());
        Trainee trainee2 = traineeDAO.create(new Trainee());

        traineeDAO.delete(trainee2);

        assertEquals(1, traineeDAO.findAll().size());
    }

    @Test
    void given2Entities_whenFindAll_thenFindAll() {
        traineeDAO.create(new Trainee());
        traineeDAO.create(new Trainee());

        assertEquals(2, traineeDAO.findAll().size());
    }

    @Test
    void givenNull_whenFindByObject_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDAO.findByObject(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithNullId_whenfindByObject_thenThrowsException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDAO.findByObject(trainee));
        assertEquals("Trainee's id can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenFindByObject_thenFindByObject() {
        Trainee trainee = traineeDAO.create(new Trainee());

        assertEquals(trainee, traineeDAO.findByObject(trainee));
    }

    @Test
    void givenNull_whenFindByUsername_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeDAO.findByUsername(null));
        assertEquals("Trainee's username can't be null", e.getMessage());
    }

    @Test
    void given0Entity_whenFindByUsername_thenNull(){
        traineeDAO.create(new Trainee());
        assertNull(traineeDAO.findByUsername("someUserName"));
    }

    @Test
    void givenEntity_whenFindByUsername_thenFound(){
        String username = "UserName";
        Trainee trainee = Trainee.builder().username(username).build();

        trainee = traineeDAO.create(trainee);

        assertEquals(trainee, traineeDAO.findByUsername(username));
    }
}