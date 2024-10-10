package orlov.programming.springcoregym.facade.user;

import orlov.programming.springcoregym.dto.trainee.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeFacade {
    Optional<Trainee> createTrainee(Trainee trainee);

    boolean isTraineeUsernamePasswordMatch(String username, String password);

    Optional<Trainee> updateTrainee(Trainee trainee);

    void deleteTrainee(String username);

    Optional<Trainee> selectTrainee(String username);

    Optional<Trainee> changeTraineePassword(String username, String newPassword);

    Optional<Trainee> activateTrainee(String username);

    List<Training> getTraineeTrainingsByTraineeTrainingDTO(TraineeTrainingDTO traineeTrainingDTO);

    void updateTraineeTrainers(Long traineeId, List<Long> trainerIds);
}
