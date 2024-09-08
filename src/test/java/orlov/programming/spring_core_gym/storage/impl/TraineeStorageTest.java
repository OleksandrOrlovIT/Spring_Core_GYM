package orlov.programming.spring_core_gym.storage.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orlov.programming.spring_core_gym.model.user.Trainee;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TraineeStorageTest {

    private static TraineeStorage traineeStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new TraineeStorage();
    }

    @Test
    void checkStorageEmpty(){
        assertTrue(traineeStorage.getStorage().isEmpty());
    }

    @Test
    void getLastKeyShouldReturn1AtDefault(){
        assertEquals(1, traineeStorage.getLastKey());
    }

    @Test
    void getLastKeyShouldReturnLastKey(){
        assertEquals(1, traineeStorage.getLastKey());
        traineeStorage.populateStorage();
        assertEquals(11, traineeStorage.getLastKey());
    }

    @Test
    void constructTraineeWorksCorrectly(){
        String[] arr = new String[]{"first", "last", "userName", "pass", "true", "1000-01-01", "address"};

        Trainee trainee = traineeStorage.constructTrainee(arr);

        assertNotNull(trainee);
        assertEquals(arr[0], trainee.getFirstName());
        assertEquals(arr[1], trainee.getLastName());
        assertEquals(arr[2], trainee.getUsername());
        assertEquals(arr[3], trainee.getPassword());
        assertEquals(true, trainee.getIsActive());
        assertEquals(LocalDate.parse(arr[5]), trainee.getDateOfBirth());
        assertEquals(arr[6], trainee.getAddress());
        assertEquals(1, trainee.getUserId());
    }

    @Test
    void populateStorageCreates10(){
        assertEquals(0, traineeStorage.getStorage().size());
        traineeStorage.populateStorage();
        assertEquals(10, traineeStorage.getStorage().size());
    }

    @Test
    void brokenFileWontPopulate(){
        traineeStorage.setFilePath("src/test/resources/traineeBroken.txt");

        assertDoesNotThrow(traineeStorage::populateStorage);

        assertEquals(0, traineeStorage.getStorage().size());
    }
}