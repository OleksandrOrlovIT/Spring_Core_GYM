package orlov.programming.springcoregym.facade;

import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.List;

public interface GYMFacade {
    Trainee createTrainee(Trainee trainee);
    boolean traineeUsernamePasswordMatch(String username, String password);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(String username);
    Trainee selectTrainee(String username);
    Trainee changeTraineePassword(String username, String newPassword);
    Trainee activateTrainee(String username);
    List<Training> getTraineeTrainingsByDateTraineeNameTrainingType(LocalDate startDate, LocalDate endDate, String userName, String trainingType);
    void updateTraineeTrainers(Long traineeId, List<Long> trainerIds);

    Trainer createTrainer(Trainer trainer);
    boolean trainerUsernamePasswordMatch(String username, String password);
    Trainer updateTrainer(Trainer trainer);
    Trainer selectTrainer(String username);
    Trainer changeTrainerPassword(String username, String newPassword);
    Trainer activateTrainer(String username);
    List<Training> getTrainingsByDateTrainerName(LocalDate startDate, LocalDate endDate, String trainerName);
    List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername);

    Training addTraining(Training training);

    void authenticateUser(String userName, String password, boolean isTrainee);
    void logOut();
}
