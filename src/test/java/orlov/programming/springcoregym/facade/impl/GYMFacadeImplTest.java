package orlov.programming.springcoregym.facade.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.configuration.AppConfig;
import orlov.programming.springcoregym.facade.GYMFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class GYMFacadeImplTest {

    @Autowired
    private GYMFacade facade;

    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String PASSWORD = "1111111111";

    @Test
    void createTrainee() {
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        Trainee savedTrainee = facade.createTrainee(trainee);

        assertNotNull(savedTrainee);
        assertEquals(savedTrainee.getFirstName(), FIRST_NAME);
        assertEquals(savedTrainee.getLastName(), LAST_NAME);
    }

    @Test
    void updateTrainee() {
        String addition = "1";
        String updatedAddress = "newAddress";
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME + addition).lastName(LAST_NAME + addition).build();

        Trainee savedTrainee = facade.createTrainee(trainee);

        savedTrainee.setAddress(updatedAddress);

        Trainee updatedTrainee = facade.updateTrainee(savedTrainee);

        assertNotNull(updatedTrainee);
        assertEquals(updatedTrainee.getAddress(), updatedAddress);
    }

    @Test
    void deleteTrainee() {
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        Trainee savedTrainee = facade.createTrainee(trainee);

        assertNotNull(facade.selectTrainee(savedTrainee));

        facade.deleteTrainee(savedTrainee);

        var e = assertThrows(NoSuchElementException.class, () -> facade.selectTrainee(savedTrainee));
        assertEquals("Trainee not found " + savedTrainee, e.getMessage());
    }

    @Test
    void selectTrainee() {
        Trainee trainee = Trainee.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        Trainee savedTrainee = facade.createTrainee(trainee);

        Trainee foundTrainee = facade.selectTrainee(savedTrainee);
        assertNotNull(foundTrainee);
        assertEquals(savedTrainee, foundTrainee);
    }

    @Test
    void createTrainer() {
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        Trainer savedTrainer = facade.createTrainer(trainer);

        assertNotNull(savedTrainer);
        assertEquals(savedTrainer.getFirstName(), FIRST_NAME);
        assertEquals(savedTrainer.getLastName(), LAST_NAME);
    }

    @Test
    void updateTrainer() {
        String addition = "1";
        String updatedSpecialization = "newSpecialization";
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME + addition).lastName(LAST_NAME + addition).build();

        Trainer savedTrainer = facade.createTrainer(trainer);

        savedTrainer.setSpecialization(updatedSpecialization);

        Trainer updatedTrainer = facade.updateTrainer(savedTrainer);

        assertNotNull(updatedTrainer);
        assertEquals(updatedTrainer.getSpecialization(), updatedSpecialization);
    }

    @Test
    void selectTrainer() {
        Trainer trainer = Trainer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        Trainer savedTrainer = facade.createTrainer(trainer);
        Trainer foundTrainer = facade.selectTrainer(savedTrainer);

        assertNotNull(foundTrainer);
        assertEquals(savedTrainer, foundTrainer);
    }

    @Test
    void createTraining() {
        Trainee trainee = Trainee.builder().userId(1L).username("John.Doe").build();
        Trainer trainer = Trainer.builder().userId(1L).username("John.Doe").build();

        trainee = facade.selectTrainee(trainee);
        trainer = facade.selectTrainer(trainer);

        Training training = Training.builder().trainee(trainee).trainer(trainer).build();
        Training savedTraining = facade.createTraining(training);
        assertEquals(training, savedTraining);
    }

    @Test
    void selectTraining() {
        Trainee trainee = Trainee.builder().userId(1L).username("John.Doe").build();
        Trainer trainer = Trainer.builder().userId(1L).username("John.Doe").build();

        trainee = facade.selectTrainee(trainee);
        trainer = facade.selectTrainer(trainer);

        Training training = Training.builder().trainee(trainee).trainer(trainer).build();
        Training savedTraining = facade.createTraining(training);
        Training foundTraining = facade.selectTraining(savedTraining);
        assertEquals(foundTraining, savedTraining);
    }
}
