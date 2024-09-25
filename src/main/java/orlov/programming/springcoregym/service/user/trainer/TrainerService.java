package orlov.programming.springcoregym.service.user.trainer;

import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.UpdatableService;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService extends UpdatableService<Trainer, Long> {

    boolean userNameMatchPassword(String username, String password);

    Trainer changePassword(Trainer trainer, String newPassword);

    Trainer activateTrainer(Long trainerId);

    Trainer deactivateTrainer(Long trainerId);

    List<Training> getTrainingsByDate(LocalDate startDate, LocalDate endDate, String userName);

    List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername);
}
