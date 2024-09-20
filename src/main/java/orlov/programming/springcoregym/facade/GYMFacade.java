package orlov.programming.springcoregym.facade;

import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

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
