package orlov.programming.springcoregym.storage.impl;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Log4j2
public class EntityStorageTest {

    @InjectMocks
    private EntityStorage entityStorage;

    @BeforeEach
    public void setUp() {
        entityStorage = new EntityStorage();
    }

    @Test
    public void testPopulateBrokenTrainees() {
        entityStorage.populateEntities(Trainee.class, "src/test/resources/traineeBroken.txt", entityStorage::constructTrainee);

        Map<Long, Trainee> traineeStorage = entityStorage.getStorage(Trainee.class);
        assertEquals(0, traineeStorage.size());
    }

    @Test
    public void testPopulateStorage() {
        entityStorage.populateStorage();

        assertFalse(entityStorage.getStorage(Trainee.class).isEmpty());
        assertFalse(entityStorage.getStorage(Trainer.class).isEmpty());
        assertFalse(entityStorage.getStorage(Training.class).isEmpty());
    }
}