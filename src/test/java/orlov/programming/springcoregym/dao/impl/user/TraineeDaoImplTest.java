package orlov.programming.springcoregym.dao.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDaoImpl;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.storage.Storage;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDaoImplTest {

    @Mock
    private Storage storage;

    @InjectMocks
    private TraineeDaoImpl traineeDaoImpl;

    @BeforeEach
    void setUp() {
        when(storage.getStorage(Trainee.class)).thenReturn(new HashMap<>());
        when(storage.getNextId(Trainee.class)).thenReturn(1L);
        traineeDaoImpl = new TraineeDaoImpl(storage);
    }

    @Test
    void givenNull_whenCreate_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDaoImpl.create(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithId_whenCreate_thenThrowsException() {
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDaoImpl.create(Trainee.builder().userId(1L).build()));
        assertEquals("Entity's id has to be null", e.getMessage());
    }

    @Test
    void givenValid_whenCreate_thenCreate() {
        Trainee trainee = new Trainee();

        Trainee savedTrainee = traineeDaoImpl.create(trainee);

        assertNotNull(savedTrainee);
        assertEquals(1, savedTrainee.getUserId());
        assertEquals(1, traineeDaoImpl.findAll().size());
    }

    @Test
    void givenNull_whenUpdate_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDaoImpl.update(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithNullId_whenUpdate_thenThrowsException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDaoImpl.update(trainee));
        assertEquals("Trainee's id can't be null", e.getMessage());
    }

    @Test
    void givenNonExistingTrainee_whenUpdate_thenThrowsException() {
        Trainee trainee = Trainee.builder().userId(1L).build();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDaoImpl.update(trainee));
        assertEquals("Entity does not exist", e.getMessage());
    }

    @Test
    void givenValid_whenUpdate_thenUpdate() {
        Trainee trainee = traineeDaoImpl.create(new Trainee());

        String userName = "UserName";
        Trainee updatedTrainee = traineeDaoImpl.update(Trainee.builder().userId(1L).username(userName).build());

        assertNotNull(updatedTrainee);
        assertEquals(trainee.getUserId(), updatedTrainee.getUserId());
        assertNotEquals(trainee.getUsername(), updatedTrainee.getUsername());
        assertEquals(userName, updatedTrainee.getUsername());
    }

    @Test
    void givenNull_whenDelete_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDaoImpl.delete(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithNullId_whenDelete_thenThrowsException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDaoImpl.delete(trainee));
        assertEquals("Trainee's id can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenDelete_thenDelete() {
        traineeDaoImpl.create(new Trainee());
        Trainee trainee2 = traineeDaoImpl.create(new Trainee());

        traineeDaoImpl.delete(trainee2);

        assertEquals(1, traineeDaoImpl.findAll().size());
    }

    @Test
    void given2Entities_whenFindAll_thenFindAll() {
        traineeDaoImpl.create(new Trainee());
        traineeDaoImpl.create(new Trainee());

        assertEquals(2, traineeDaoImpl.findAll().size());
    }

    @Test
    void givenNull_whenFindByObject_thenThrowsException() {
        var e = assertThrows(NullPointerException.class, () -> traineeDaoImpl.findByObject(null));
        assertEquals("Trainee can't be null", e.getMessage());
    }

    @Test
    void givenTraineeWithNullId_whenfindByObject_thenThrowsException() {
        Trainee trainee = new Trainee();
        var e = assertThrows(IllegalArgumentException.class, () -> traineeDaoImpl.findByObject(trainee));
        assertEquals("Trainee's id can't be null", e.getMessage());
    }

    @Test
    void givenValid_whenFindByObject_thenFindByObject() {
        Trainee trainee = traineeDaoImpl.create(new Trainee());

        assertEquals(Optional.of(trainee), traineeDaoImpl.findByObject(trainee));
    }

    @Test
    void givenNull_whenFindByUsername_thenException(){
        var e = assertThrows(NullPointerException.class, () -> traineeDaoImpl.findByUsername(null));
        assertEquals("Username can't be null", e.getMessage());
    }

    @Test
    void given0Entity_whenFindByUsername_thenNull(){
        traineeDaoImpl.create(new Trainee());
        assertEquals(Optional.empty(), traineeDaoImpl.findByUsername("someUserName"));
    }

    @Test
    void givenEntity_whenFindByUsername_thenFound(){
        String username = "UserName";
        Trainee trainee = Trainee.builder().username(username).build();

        trainee = traineeDaoImpl.create(trainee);

        assertEquals(Optional.of(trainee), traineeDaoImpl.findByUsername(username));
    }

    @Test
    void givenHashmapWithNull_whenFindByUsername_thenReturnsEmpty() {
        HashMap<Long, Trainee> hashmap = new HashMap<>();
        hashmap.put(1L, null);

        when(storage.getStorage(Trainee.class)).thenReturn(hashmap);
        traineeDaoImpl = new TraineeDaoImpl(storage);

        String username = "UserName";
        assertEquals(Optional.empty(), traineeDaoImpl.findByUsername(username));
    }
}
