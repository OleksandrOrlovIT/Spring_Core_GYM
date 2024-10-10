package orlov.programming.springcoregym.dao.impl.user.trainee;

import orlov.programming.springcoregym.dao.DaoUsernameFindable;
import orlov.programming.springcoregym.dto.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;

/**
 * DAO interface for managing {@link Trainee} entities with additional methods specific to trainees.
 */
public interface TraineeDao extends DaoUsernameFindable<Trainee, Long> {

    /**
     * Retrieves a list of {@link Training} entities associated with a trainee, filtered by the
     * provided {@link TraineeTrainingDTO}.
     *
     * @param traineeTrainingDTO DTO containing filtering criteria such as username, training type, and date range
     * @return a list of matching {@link Training} entities
     */
    List<Training> getTrainingsByTraineeTrainingDTO(TraineeTrainingDTO traineeTrainingDTO);

    /**
     * Deletes a {@link Trainee} entity by its username.
     *
     * @param username the username of the trainee to delete
     */
    void deleteByUsername(String username);
}
