package orlov.programming.springcoregym.service.authentication.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.model.user.User;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.service.user.trainee.TraineeService;
import orlov.programming.springcoregym.service.user.trainer.TrainerService;

@Service
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private static ThreadLocal<User> loggedUser = ThreadLocal.withInitial(() -> null);

    public AuthenticationServiceImpl(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Override
    public void authenticateUser(String userName, String password, boolean isTrainee) {
        try {
            if (isTrainee) {
                User trainee = traineeService.authenticateTrainee(userName, password);
                loggedUser.set(trainee);
            } else {
                User trainer = trainerService.authenticateTrainer(userName, password);
                loggedUser.set(trainer);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void logOut() {
        isUserLogged();
        loggedUser = ThreadLocal.withInitial(() -> null);
    }

    @Override
    public void isUserLogged() {
        if (loggedUser == null || loggedUser.get() == null) {
            throw new IllegalArgumentException("You are not logged in");
        }
    }

    @Override
    public User getLoggedUser() {
        isUserLogged();
        return loggedUser.get();
    }
}
