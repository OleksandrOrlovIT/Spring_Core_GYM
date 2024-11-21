package ua.orlov.springcoregym.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.service.training.TrainingTypeService;
import ua.orlov.springcoregym.service.user.trainee.TraineeService;
import ua.orlov.springcoregym.service.user.trainer.TrainerService;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class BootstrapLoader implements CommandLineRunner {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Override
    public void run(String... args) throws Exception {
        Trainee trainee = new Trainee();
        trainee.setFirstName("a");
        trainee.setLastName("a");
        trainee.setPassword("1111111111");
        trainee.setAddress("address");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.of(2020, 10, 10));

        traineeService.create(trainee);

        Trainer trainer = new Trainer();
        trainer.setFirstName("a");
        trainer.setLastName("b");
        trainer.setPassword("1111111111");
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeService.getAll().get(0));
        trainerService.create(trainer);
    }
}
