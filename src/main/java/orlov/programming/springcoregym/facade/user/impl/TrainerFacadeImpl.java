package orlov.programming.springcoregym.facade.user.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.facade.user.TrainerFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.service.user.trainer.TrainerService;
import orlov.programming.springcoregym.util.model.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Log4j2
@AllArgsConstructor
public class TrainerFacadeImpl implements TrainerFacade {

    private final TrainerService trainerService;
    private final AuthenticationService authenticationService;

    @Override
    public Optional<Trainer> createTrainer(Trainer trainer) {
        try {
            return Optional.ofNullable(trainerService.create(trainer));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean isTrainerUsernamePasswordMatch(String username, String password) {
        try {
            authenticationService.isUserLogged();
            return trainerService.isUserNameMatchPassword(username, password);
        } catch (Exception e) {
            log.error(e);
        }

        return false;
    }

    @Override
    public Optional<Trainer> updateTrainer(Trainer trainer) {
        try {
            authenticationService.isUserLogged();
            Trainer updatedTrainer = trainerService.update(trainer);
            return Optional.ofNullable(trainerService.getByUserNameWithTrainees(updatedTrainer.getUsername()));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Trainer> selectTrainer(String username) {
        try {
            authenticationService.isUserLogged();
            return Optional.ofNullable(trainerService.getByUserNameWithTrainees(username));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Trainer> changeTrainerPassword(String username, String newPassword) {
        try {
            authenticationService.isUserLogged();
            Trainer trainer = trainerService.getByUsername(username);
            return Optional.ofNullable(trainerService.changePassword(trainer, newPassword));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Trainer> activateTrainer(String username) {
        try {
            authenticationService.isUserLogged();
            Trainer trainer = trainerService.getByUsername(username);
            return Optional.ofNullable(trainerService.activateTrainer(trainer.getId()));
        } catch (Exception e) {
            log.error(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Training> getTrainingsByDateTrainerName(LocalDate startDate, LocalDate endDate, String trainerName) {
        authenticationService.isUserLogged();
        return trainerService.getTrainingsByDate(startDate, endDate, trainerName);
    }

    @Override
    public List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername, Pageable pageable) {
        authenticationService.isUserLogged();
        return trainerService.getTrainersWithoutPassedTrainee(traineeUsername, pageable);
    }
}
