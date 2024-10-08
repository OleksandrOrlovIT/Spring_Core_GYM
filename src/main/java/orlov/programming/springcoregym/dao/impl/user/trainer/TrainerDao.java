package orlov.programming.springcoregym.dao.impl.user.trainer;

import orlov.programming.springcoregym.dao.DaoUsernameFindable;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.util.model.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TrainerDao extends DaoUsernameFindable<Trainer, Long> {
    List<Training> getTrainingsByDateAndUsername(LocalDate startDate, LocalDate endDate, String userName);

    List<Trainer> getTrainersWithoutPassedTrainee(Trainee trainee, Pageable pageable);

    List<Trainer> getByIds(List<Long> trainerIds);
}
