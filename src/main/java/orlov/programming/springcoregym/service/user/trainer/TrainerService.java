package orlov.programming.springcoregym.service.user.trainer;

import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService {

    Trainer create(Trainer trainer);

    Trainer select(Long id);

    List<Trainer> getAll();

    Trainer update(Trainer trainer);

    boolean isUserNameMatchPassword(String username, String password);

    Trainer changePassword(Trainer trainer, String newPassword);

    Trainer activateTrainer(Long trainerId);

    Trainer deactivateTrainer(Long trainerId);

    List<Training> getTrainingsByDate(LocalDate startDate, LocalDate endDate, String userName);

    List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername);

    Trainer authenticateTrainer(String userName, String password);

    Trainer getByUsername(String trainerUserName);
}
