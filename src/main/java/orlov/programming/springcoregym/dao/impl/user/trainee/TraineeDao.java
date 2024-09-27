package orlov.programming.springcoregym.dao.impl.user.trainee;

import orlov.programming.springcoregym.dao.DaoUsernameFindable;
import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;

public interface TraineeDao extends DaoUsernameFindable<Trainee, Long> {
    List<Training> getTrainingsByDateUsernameTrainingType(TraineeTrainingDTO traineeTrainingDTO);
    void deleteByUsername(String username);
}
