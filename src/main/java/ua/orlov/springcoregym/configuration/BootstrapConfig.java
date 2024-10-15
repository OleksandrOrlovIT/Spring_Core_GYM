package ua.orlov.springcoregym.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.training.TrainingType;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.service.training.TrainingService;
import ua.orlov.springcoregym.service.training.TrainingTypeService;
import ua.orlov.springcoregym.service.user.trainee.TraineeService;
import ua.orlov.springcoregym.service.user.trainer.TrainerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Configuration
@AllArgsConstructor
public class BootstrapConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (traineeService.getAll().isEmpty()) {
            Trainee testTrainee = Trainee.builder()
                    .username("trainee1")
                    .firstName("first")
                    .lastName("last")
                    .password("pass")
                    .isActive(true)
                    .build();
            testTrainee = traineeService.create(testTrainee);

            Optional<TrainingType> optionalTrainingType = trainingTypeService.getAll().stream().findFirst();

            if (optionalTrainingType.isPresent()) {
                TrainingType trainingType = optionalTrainingType.get();

                Trainer testTrainer1 = Trainer.builder()
                        .username("trainer1")
                        .firstName("trainer1")
                        .lastName("trainer1")
                        .password("pass")
                        .specialization(trainingType)
                        .isActive(true)
                        .build();

                Trainer testTrainer2 = Trainer.builder()
                        .username("trainer2")
                        .firstName("trainer2")
                        .lastName("trainer2")
                        .password("pass")
                        .specialization(trainingType)
                        .isActive(true)
                        .build();

                Trainer testTrainer3 = Trainer.builder()
                        .username("trainer3")
                        .firstName("trainer3")
                        .lastName("trainer3")
                        .password("pass")
                        .specialization(trainingType)
                        .isActive(true)
                        .build();


                Trainer testTrainer4 = Trainer.builder()
                        .username("trainer4")
                        .firstName("trainer4")
                        .lastName("trainer4")
                        .password("pass")
                        .specialization(trainingType)
                        .isActive(true)
                        .build();

                testTrainer1 = trainerService.create(testTrainer1);
                testTrainer2 = trainerService.create(testTrainer2);
                testTrainer3 = trainerService.create(testTrainer3);
                testTrainer4 = trainerService.create(testTrainer4);

                Training testTraining1 = Training.builder()
                        .trainee(testTrainee)
                        .trainer(testTrainer1)
                        .trainingName("Training 1")
                        .trainingType(trainingType)
                        .trainingDate(LocalDate.of(2020, 10, 10))
                        .trainingDuration(10L)
                        .build();

                Training testTraining2 = Training.builder()
                        .trainee(testTrainee)
                        .trainer(testTrainer2)
                        .trainingName("Training 2")
                        .trainingType(trainingType)
                        .trainingDate(LocalDate.of(2020, 10, 10))
                        .trainingDuration(10L)
                        .build();

                trainingService.create(testTraining1);
                trainingService.create(testTraining2);

                traineeService.updateTraineeTrainers(testTrainee.getId(), List.of(testTrainer1.getId(), testTrainer2.getId()));
            } else {
                System.out.println("No training types available to assign to trainers.");
            }
        }
    }
}
