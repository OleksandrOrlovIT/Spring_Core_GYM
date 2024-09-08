package orlov.programming.spring_core_gym.facade.impl;

import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.facade.GYMFacade;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.service.CRUDService;
import orlov.programming.spring_core_gym.service.CSService;
import orlov.programming.spring_core_gym.service.UpdatableService;

@Component
public class GYMFacadeImpl implements GYMFacade {

    private final CRUDService<Trainee> traineeCRUDService;
    private final UpdatableService<Trainer> trainerUpdatableService;
    private final CSService<Training> trainingCSService;


    public GYMFacadeImpl(CRUDService<Trainee> traineeCRUDService, UpdatableService<Trainer> trainerUpdatableService, CSService<Training> trainingCSService) {
        this.traineeCRUDService = traineeCRUDService;
        this.trainerUpdatableService = trainerUpdatableService;
        this.trainingCSService = trainingCSService;
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return traineeCRUDService.create(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeCRUDService.update(trainee);
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        traineeCRUDService.delete(trainee);
    }

    @Override
    public Trainee selectTrainee(Trainee trainee) {
        return traineeCRUDService.select(trainee);
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerUpdatableService.create(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerUpdatableService.update(trainer);
    }

    @Override
    public Trainer selectTrainer(Trainer trainer) {
        return trainerUpdatableService.select(trainer);
    }

    @Override
    public Training createTraining(Training training) {
        return trainingCSService.create(training);
    }

    @Override
    public Training selectTraining(Training training) {
        return trainingCSService.select(training);
    }
}