//package orlov.programming.springcoregym.dao.impl.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDaoImpl;
//import orlov.programming.springcoregym.model.user.Trainer;
//import orlov.programming.springcoregym.storage.Storage;
//
//import java.util.HashMap;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class TrainerDaoImplTest {
//
//    @Mock
//    private Storage storage;
//
//    @InjectMocks
//    private TrainerDaoImpl trainerDaoImpl;
//
//    @BeforeEach
//    void setUp() {
//        when(storage.getStorage(Trainer.class)).thenReturn(new HashMap<>());
//        when(storage.getNextId(Trainer.class)).thenReturn(1L);
//        trainerDaoImpl = new TrainerDaoImpl(storage);
//    }
//
//    @Test
//    void givenNull_whenCreate_thenThrowsException() {
//        var e = assertThrows(NullPointerException.class, () -> trainerDaoImpl.create(null));
//        assertEquals("Trainer can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenTrainerWithId_whenCreate_thenThrowsException() {
//        var e = assertThrows(IllegalArgumentException.class, () -> trainerDaoImpl.create(Trainer.builder().userId(1L).build()));
//        assertEquals("Entity's id has to be null", e.getMessage());
//    }
//
//    @Test
//    void givenValid_whenCreate_thenCreate() {
//        Trainer trainer = new Trainer();
//
//        Trainer savedTrainer = trainerDaoImpl.create(trainer);
//
//        assertNotNull(savedTrainer);
//        assertEquals(1, savedTrainer.getId());
//        assertEquals(1, trainerDaoImpl.findAll().size());
//    }
//
//    @Test
//    void givenNull_whenUpdate_thenThrowsException() {
//        var e = assertThrows(NullPointerException.class, () -> trainerDaoImpl.update(null));
//        assertEquals("Trainer can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenTrainerWithNullId_whenUpdate_thenThrowsException() {
//        Trainer trainer = new Trainer();
//        var e = assertThrows(IllegalArgumentException.class, () -> trainerDaoImpl.update(trainer));
//        assertEquals("Trainer's id can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenNonExistingTrainer_whenUpdate_thenThrowsException() {
//        Trainer trainer = Trainer.builder().userId(1L).build();
//        var e = assertThrows(IllegalArgumentException.class, () -> trainerDaoImpl.update(trainer));
//        assertEquals("Entity does not exist", e.getMessage());
//    }
//
//    @Test
//    void givenValid_whenUpdate_thenUpdate() {
//        Trainer trainer = trainerDaoImpl.create(new Trainer());
//
//        String userName = "UserName";
//        Trainer updatedTrainer = trainerDaoImpl.update(Trainer.builder().userId(1L).username(userName).build());
//
//        assertNotNull(updatedTrainer);
//        assertEquals(trainer.getId(), updatedTrainer.getId());
//        assertNotEquals(trainer.getUsername(), updatedTrainer.getUsername());
//        assertEquals(userName, updatedTrainer.getUsername());
//    }
//
//    @Test
//    void givenNull_whenDelete_thenThrowsException() {
//        var e = assertThrows(NullPointerException.class, () -> trainerDaoImpl.delete(null));
//        assertEquals("Trainer can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenTrainerWithNullId_whenDelete_thenThrowsException() {
//        Trainer trainer = new Trainer();
//        var e = assertThrows(IllegalArgumentException.class, () -> trainerDaoImpl.delete(trainer));
//        assertEquals("Trainer's id can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenValid_whenDelete_thenDelete() {
//        trainerDaoImpl.create(new Trainer());
//        Trainer trainer2 = trainerDaoImpl.create(new Trainer());
//
//        trainerDaoImpl.delete(trainer2);
//
//        assertEquals(1, trainerDaoImpl.findAll().size());
//    }
//
//    @Test
//    void given2Entities_whenFindAll_thenFindAll() {
//        trainerDaoImpl.create(new Trainer());
//        trainerDaoImpl.create(new Trainer());
//
//        assertEquals(2, trainerDaoImpl.findAll().size());
//    }
//
//    @Test
//    void givenNull_whenFindByObject_thenThrowsException() {
//        var e = assertThrows(NullPointerException.class, () -> trainerDaoImpl.findByObject(null));
//        assertEquals("Trainer can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenTrainerWithNullId_whenfindByObject_thenThrowsException() {
//        Trainer trainer = new Trainer();
//        var e = assertThrows(IllegalArgumentException.class, () -> trainerDaoImpl.findByObject(trainer));
//        assertEquals("Trainer's id can't be null", e.getMessage());
//    }
//
//    @Test
//    void givenValid_whenFindByObject_thenFindByObject() {
//        Trainer trainer = trainerDaoImpl.create(new Trainer());
//
//        assertEquals(Optional.of(trainer), trainerDaoImpl.findByObject(trainer));
//    }
//
//    @Test
//    void givenNull_whenFindByUsername_thenException(){
//        var e = assertThrows(NullPointerException.class, () -> trainerDaoImpl.findByUsername(null));
//        assertEquals("Username can't be null", e.getMessage());
//    }
//
//    @Test
//    void given0Entity_whenFindByUsername_thenNull(){
//        trainerDaoImpl.create(new Trainer());
//        assertEquals(Optional.empty(), trainerDaoImpl.findByUsername("someUserName"));
//    }
//
//    @Test
//    void givenEntity_whenFindByUsername_thenFound(){
//        String username = "UserName";
//        Trainer trainer = Trainer.builder().username(username).build();
//
//        trainer = trainerDaoImpl.create(trainer);
//
//        assertEquals(Optional.of(trainer), trainerDaoImpl.findByUsername(username));
//    }
//}
