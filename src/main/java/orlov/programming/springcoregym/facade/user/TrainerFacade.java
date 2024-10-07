package orlov.programming.springcoregym.facade.user;

import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerFacade {
    Optional<Trainer> createTrainer(Trainer trainer);

    boolean isTrainerUsernamePasswordMatch(String username, String password);

    Optional<Trainer> updateTrainer(Trainer trainer);

    Optional<Trainer> selectTrainer(String username);

    Optional<Trainer> changeTrainerPassword(String username, String newPassword);

    Optional<Trainer> activateTrainer(String username);

    List<Training> getTrainingsByDateTrainerName(LocalDate startDate, LocalDate endDate, String trainerName);

    List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername);
}
