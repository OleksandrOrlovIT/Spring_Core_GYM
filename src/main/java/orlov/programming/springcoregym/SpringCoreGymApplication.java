package orlov.programming.springcoregym;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import orlov.programming.springcoregym.configuration.AppConfig;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.training.TrainingService;
import orlov.programming.springcoregym.service.training.TrainingTypeService;
import orlov.programming.springcoregym.service.user.trainee.TraineeService;
import orlov.programming.springcoregym.service.user.trainer.TrainerService;

import java.time.LocalDate;

public class SpringCoreGymApplication{

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
