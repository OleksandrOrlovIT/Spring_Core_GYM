package orlov.programming.spring_core_gym.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.dao.impl.user.DAOUsernameFindable;
import orlov.programming.spring_core_gym.facade.GYMFacade;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;

@Component
public class GYMFacadeImpl implements GYMFacade {

    private final DAOUsernameFindable<Trainee> traineeDAO;
    private final DAOUsernameFindable<Trainer> trainerDAO;
    private final DAO<Training> trainingDAO;

    @Autowired
    public GYMFacadeImpl(DAOUsernameFindable<Trainee> traineeDAO, DAOUsernameFindable<Trainer> trainerDAO, DAO<Training> trainingDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return traineeDAO.create(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeDAO.update(trainee);
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        traineeDAO.delete(trainee);
    }

    @Override
    public Trainee selectTrainee(Trainee trainee) {
        return traineeDAO.findByObject(trainee);
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerDAO.update(trainer);
    }

    @Override
    public Trainer selectTrainer(Trainer trainer) {
        return trainerDAO.findByObject(trainer);
    }

    @Override
    public Training createTraining(Training training) {
        return trainingDAO.create(training);
    }

    @Override
    public Training selectTraining(Training training) {
        return trainingDAO.findByObject(training);
    }
}