package orlov.programming.springcoregym.service.user.trainee;

import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.CRUDService;

import java.time.LocalDate;
import java.util.List;

public interface TraineeService extends CRUDService<Trainee, Long> {
    boolean userNameMatchPassword(String username, String password);

    Trainee changePassword(Trainee trainee, String newPassword);

    Trainee activateTrainee(Long traineeId);

    Trainee deactivateTrainee(Long traineeId);

    List<Training> getTrainingsByDate(LocalDate startDate, LocalDate endDate, String userName);
}
