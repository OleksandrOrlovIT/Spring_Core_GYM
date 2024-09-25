package orlov.programming.springcoregym.facade.impl;

import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.facade.GYMFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.training.TrainingService;
import orlov.programming.springcoregym.service.user.trainee.TraineeService;
import orlov.programming.springcoregym.service.user.trainer.TrainerService;

@Component
public class GYMFacadeImpl implements GYMFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;


    public GYMFacadeImpl(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.update(trainee);
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        traineeService.delete(trainee);
    }

    @Override
    public Trainee selectTrainee(Trainee trainee) {
        return traineeService.select(trainee.getId());
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.update(trainer);
    }

    @Override
    public Trainer selectTrainer(Trainer trainer) {
        return trainerService.select(trainer.getId());
    }

    @Override
    public Training createTraining(Training training) {
        return trainingService.create(training);
    }

    @Override
    public Training selectTraining(Training training) {
        return trainingService.select(training.getId());
    }
}
