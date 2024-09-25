package orlov.programming.springcoregym.dao.impl.user.trainee;

import orlov.programming.springcoregym.dao.DaoUsernameFindable;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.time.LocalDate;
import java.util.List;

public interface TraineeDao extends DaoUsernameFindable<Trainee, Long> {
    List<Training> getTrainingsByDateUsernameTrainingType
            (LocalDate startDate, LocalDate endDate, String userName, String trainingType);
    void deleteByUsername(String username);
}
