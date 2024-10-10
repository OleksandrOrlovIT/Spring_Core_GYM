package orlov.programming.springcoregym.facade.user.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.dto.trainee.TraineeTrainingDTO;
import orlov.programming.springcoregym.facade.user.TraineeFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.service.user.trainee.TraineeService;

import java.util.List;
import java.util.Optional;

@Component
@Log4j2
@AllArgsConstructor
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;
    private final AuthenticationService authenticationService;

    @Override
    public Optional<Trainee> createTrainee(Trainee trainee) {
        try {
            return Optional.ofNullable(traineeService.create(trainee));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean isTraineeUsernamePasswordMatch(String username, String password) {
        try {
            authenticationService.isUserLogged();
            return traineeService.isUserNameMatchPassword(username, password);
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @Override
    public Optional<Trainee> updateTrainee(Trainee trainee) {
        try {
            authenticationService.isUserLogged();
            return Optional.ofNullable(traineeService.update(trainee));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public void deleteTrainee(String username) {
        authenticationService.isUserLogged();
        traineeService.deleteByUsername(username);
    }

    @Override
    public Optional<Trainee> selectTrainee(String username) {
        try {
            authenticationService.isUserLogged();
            return Optional.ofNullable(traineeService.getByUsername(username));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Trainee> changeTraineePassword(String username, String newPassword) {
        try {
            authenticationService.isUserLogged();
            Trainee trainee = traineeService.getByUsername(username);

            if (!authenticationService.getLoggedUser().getUsername().equals(trainee.getUsername())) {
                throw new IllegalArgumentException("User tried to change password of different user");
            }

            return Optional.ofNullable(traineeService.changePassword(trainee, newPassword));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Trainee> activateTrainee(String username) {
        try {
            authenticationService.isUserLogged();
            Trainee trainee = traineeService.getByUsername(username);

            return Optional.ofNullable(traineeService.activateTrainee(trainee.getId()));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Training> getTraineeTrainingsByTraineeTrainingDTO(TraineeTrainingDTO traineeTrainingDTO) {
        authenticationService.isUserLogged();
        return traineeService.getTrainingsByTraineeTrainingDTO(traineeTrainingDTO);
    }

    @Override
    public void updateTraineeTrainers(Long traineeId, List<Long> trainerIds) {
        try {
            authenticationService.isUserLogged();
            traineeService.updateTraineeTrainers(traineeId, trainerIds);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
