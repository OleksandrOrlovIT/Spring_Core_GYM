package orlov.programming.springcoregym.service.user.trainee;

import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;

public interface TraineeService {

    Trainee create(Trainee e);

    Trainee select(Long id);

    List<Trainee> findAll();

    Trainee update(Trainee trainee);

    void deleteByUsername(String userName);

    boolean userNameMatchPassword(String username, String password);

    Trainee changePassword(Trainee trainee, String newPassword);

    Trainee activateTrainee(Long traineeId);

    Trainee deactivateTrainee(Long traineeId);

    List<Training> getTrainingsByDateTraineeNameTrainingType(TraineeTrainingDTO traineeTrainingDTO);

    Trainee authenticateTrainee(String userName, String password);

    Trainee findByUsername(String traineeUsername);

    void updateTraineeTrainers(Long traineeId, List<Long> trainerIds);
}
