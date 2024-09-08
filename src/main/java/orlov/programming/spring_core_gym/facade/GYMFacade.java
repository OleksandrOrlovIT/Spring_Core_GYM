package orlov.programming.spring_core_gym.facade;

import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;

public interface GYMFacade {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(Trainee trainee);
    Trainee selectTrainee(Trainee trainee);

    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    Trainer selectTrainer(Trainer trainer);

    Training createTraining(Training training);
    Training selectTraining(Training training);
}
