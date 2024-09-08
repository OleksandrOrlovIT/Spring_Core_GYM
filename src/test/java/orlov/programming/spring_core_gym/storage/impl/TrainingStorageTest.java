package orlov.programming.spring_core_gym.storage.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


class TrainingStorageTest {

    private TraineeStorage traineeStorage;

    private TrainerStorage trainerStorage;

    private TrainingStorage trainingStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new TraineeStorage();
        traineeStorage.populateStorage();
        trainerStorage = new TrainerStorage();
        trainerStorage.populateStorage();

        trainingStorage = new TrainingStorage(traineeStorage, trainerStorage);
    }

    @Test
    void testGetLastKeyShouldReturnNextId() {
        assertEquals(1L, trainingStorage.getLastKey());
        trainingStorage.populateStorage();
        assertEquals(11L, trainingStorage.getLastKey());
    }

    @Test
    void brokenFileWontPopulate(){
        trainingStorage.setFilePath("src/test/resources/trainingBroken.txt");

        assertDoesNotThrow(trainingStorage::populateStorage);

        assertEquals(0, trainingStorage.getStorage().size());
    }

    @Test
    void constructTrainingWorksCorrectly(){
        String[] arr = new String[]{"1", "1", "Strength Training", "STRENGTH", "2024-10-01", "01:00"};

        traineeStorage.getStorage().put(1L, Trainee.builder().userId(1L).build());
        trainerStorage.getStorage().put(1L, Trainer.builder().userId(1L).build());
        trainingStorage = new TrainingStorage(traineeStorage, trainerStorage);

        Training training = trainingStorage.constructTraining(arr);

        assertNotNull(training);
        assertEquals(Long.parseLong(arr[0]), training.getTrainee().getUserId());
        assertEquals(Long.parseLong(arr[0]), training.getTrainer().getUserId());
        assertEquals(arr[2], training.getTrainingName());
        assertEquals(arr[3], training.getTrainingType().name());
        assertEquals(LocalDate.parse(arr[4]), training.getTrainingDate());
        assertEquals(LocalTime.parse(arr[5]), training.getTrainingDuration());
    }
}