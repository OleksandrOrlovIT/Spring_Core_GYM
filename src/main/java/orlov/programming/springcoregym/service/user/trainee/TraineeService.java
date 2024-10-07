package orlov.programming.springcoregym.service.user.trainee;

import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;

public interface TraineeService {

    Trainee create(Trainee e);

    Trainee select(Long id);

    List<Trainee> getAll();

    Trainee update(Trainee trainee);

    void deleteByUsername(String userName);

    boolean isUserNameMatchPassword(String username, String password);

    Trainee changePassword(Trainee trainee, String newPassword);

    Trainee activateTrainee(Long traineeId);

    Trainee deactivateTrainee(Long traineeId);

    List<Training> getTrainingsByTraineeTrainingDTO(TraineeTrainingDTO traineeTrainingDTO);

    Trainee authenticateTrainee(String userName, String password);

    Trainee getByUsername(String traineeUsername);

    void updateTraineeTrainers(Long traineeId, List<Long> trainerIds);
}
