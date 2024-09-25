package orlov.programming.springcoregym.facade.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.bootstrap.TrainingTypeLoader;
import orlov.programming.springcoregym.facade.GYMFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.model.user.User;
import orlov.programming.springcoregym.service.training.TrainingService;
import orlov.programming.springcoregym.service.user.trainee.TraineeService;
import orlov.programming.springcoregym.service.user.trainer.TrainerService;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Component
public class GYMFacadeImpl implements GYMFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeLoader trainingTypeLoader;

    private User loggedUser;

    public GYMFacadeImpl(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService,
                         TrainingTypeLoader trainingTypeLoader) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeLoader = trainingTypeLoader;
        trainingTypeLoader.loadDefaultTrainingTypes();
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        try {
            return traineeService.create(trainee);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public boolean traineeUsernamePasswordMatch(String username, String password) {
        try {
            isUserLogged();
            return traineeService.userNameMatchPassword(username, password);
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        try {
            isUserLogged();
            return traineeService.update(trainee);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public void deleteTrainee(String username) {
        isUserLogged();
        traineeService.deleteByUsername(username);
    }

    @Override
    public Trainee selectTrainee(String username) {
        try {
            isUserLogged();
            return traineeService.findByUsername(username);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public Trainee changeTraineePassword(String username, String newPassword) {
        try {
            isUserLogged();
            Trainee trainee = traineeService.findByUsername(username);

            if(!loggedUser.getUsername().equals(trainee.getUsername())) {
                throw new IllegalArgumentException("User tried to change password of different user");
            }

            trainee = traineeService.changePassword(trainee, newPassword);

            return trainee;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public Trainee activateTrainee(String username) {
        try {
            isUserLogged();
            Trainee trainee = traineeService.findByUsername(username);

            trainee = traineeService.activateTrainee(trainee.getId());

            return trainee;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<Training> getTraineeTrainingsByDateTraineeNameTrainingType(LocalDate startDate, LocalDate endDate,
                                                                           String userName, String trainingType) {
        isUserLogged();
        return traineeService.getTrainingsByDateTraineeNameTrainingType(startDate, endDate, userName, trainingType);
    }

    @Override
    public void updateTraineeTrainers(Long traineeId, List<Long> trainerIds) {
        try {
            isUserLogged();
            traineeService.updateTraineeTrainers(traineeId, trainerIds);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        try {
            return trainerService.create(trainer);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public boolean trainerUsernamePasswordMatch(String username, String password) {
        try {
            isUserLogged();
            return trainerService.userNameMatchPassword(username, password);
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        try {
            isUserLogged();
            return trainerService.update(trainer);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public Trainer selectTrainer(String username) {
        try {
            isUserLogged();
            return trainerService.findByUsername(username);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public Trainer changeTrainerPassword(String username, String newPassword) {
        try {
            isUserLogged();
            Trainer trainer = trainerService.findByUsername(username);
            return trainerService.changePassword(trainer, newPassword);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public Trainer activateTrainer(String username) {
        try {
            isUserLogged();
            Trainer trainer = trainerService.findByUsername(username);
            return trainerService.activateTrainer(trainer.getId());
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<Training> getTrainingsByDateTrainerName(LocalDate startDate, LocalDate endDate, String trainerName) {
        isUserLogged();
        return trainerService.getTrainingsByDate(startDate, endDate, trainerName);
    }

    @Override
    public List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername) {
        isUserLogged();
        return trainerService.getTrainersWithoutPassedTrainee(traineeUsername);
    }

    @Override
    public Training addTraining(Training training) {
        isUserLogged();
        return trainingService.create(training);
    }

    @Override
    public void authenticateUser(String userName, String password, boolean isTrainee) {
        try {
            if(isTrainee){
                loggedUser = traineeService.authenticateTrainee(userName, password);
            } else {
                loggedUser = trainerService.authenticateTrainer(userName, password);
            }
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public void logOut() {
        isUserLogged();
        loggedUser = null;
    }

    private void isUserLogged(){
        if(loggedUser == null) {
            throw new IllegalArgumentException("You are not logged in");
        }
    }
}
