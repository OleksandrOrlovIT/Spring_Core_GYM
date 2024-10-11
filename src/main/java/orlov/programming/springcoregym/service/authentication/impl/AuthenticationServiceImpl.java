package orlov.programming.springcoregym.service.authentication.impl;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.model.user.User;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.service.user.trainee.TraineeService;
import orlov.programming.springcoregym.service.user.trainer.TrainerService;

import java.util.concurrent.atomic.AtomicReference;

@Service
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private static final AtomicReference<User> loggedUser = new AtomicReference<>();

    @Autowired
    public AuthenticationServiceImpl(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Override
    public boolean authenticateUser(String userName, String password, boolean isTrainee) {
        try {
            User user = isTrainee
                    ? traineeService.authenticateTrainee(userName, password)
                    : trainerService.authenticateTrainer(userName, password);

            loggedUser.set(user);
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean authenticateUser(String userName, String password) {
        try {
            if(authenticateUser(userName, password, true)){
                return true;
            } else {
                return authenticateUser(userName, password, false);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public void logOut() {
        loggedUser.set(null);
    }

    @Override
    public void isUserLogged() {
        if (loggedUser.get() == null) {
            throw new IllegalArgumentException("You are not logged in");
        }
    }

    private void isLoggedUserChangingLogin(String userName) {
        isUserLogged();

        User user = loggedUser.get();
        if (userName == null || !userName.equals(user.getUsername())) {
            throw new IllegalArgumentException("You are not logged in as " + userName);
        }
    }

    @Override
    public User getLoggedUser() {
        isUserLogged();
        return loggedUser.get();
    }

    @Override
    public boolean changeLogin(String userName, String oldPassword, String newPassword) {
        isLoggedUserChangingLogin(userName);

        try {
            Trainee foundTrainee = traineeService.getByUsername(userName);
            if (traineeService.isUserNameMatchPassword(userName, oldPassword)) {
                traineeService.changePassword(foundTrainee, newPassword);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        try {
            Trainer foundTrainer = trainerService.getByUsername(userName);
            if (trainerService.isUserNameMatchPassword(userName, oldPassword)) {
                trainerService.changePassword(foundTrainer, newPassword);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }
}
