package orlov.programming.spring_core_gym.storage.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orlov.programming.spring_core_gym.model.user.Trainer;

import static org.junit.jupiter.api.Assertions.*;

class TrainerStorageTest {
    private static TrainerStorage trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new TrainerStorage();
    }

    @Test
    void checkStorageEmpty(){
        assertTrue(trainerStorage.getStorage().isEmpty());
    }

    @Test
    void getLastKeyShouldReturn1AtDefault(){
        assertEquals(1, trainerStorage.getLastKey());
    }

    @Test
    void getLastKeyShouldReturnLastKey(){
        assertEquals(1, trainerStorage.getLastKey());
        trainerStorage.populateStorage();
        assertEquals(11, trainerStorage.getLastKey());
    }

    @Test
    void constructTrainerWorksCorrectly(){
        String[] arr = new String[]{"first", "last", "userName", "pass", "true", "spec"};

        Trainer trainer = trainerStorage.constructTrainer(arr);

        assertNotNull(trainer);
        assertEquals(arr[0], trainer.getFirstName());
        assertEquals(arr[1], trainer.getLastName());
        assertEquals(arr[2], trainer.getUsername());
        assertEquals(arr[3], trainer.getPassword());
        assertEquals(true, trainer.getIsActive());
        assertEquals(arr[5], trainer.getSpecialization());
        assertEquals(1, trainer.getUserId());
    }

    @Test
    void populateStorageCreates10(){
        assertEquals(0, trainerStorage.getStorage().size());
        trainerStorage.populateStorage();
        assertEquals(10, trainerStorage.getStorage().size());
    }

    @Test
    void brokenFileWontPopulate(){
        trainerStorage.setFilePath("src/test/resources/trainerBroken.txt");

        assertDoesNotThrow(trainerStorage::populateStorage);

        assertEquals(0, trainerStorage.getStorage().size());
    }
}