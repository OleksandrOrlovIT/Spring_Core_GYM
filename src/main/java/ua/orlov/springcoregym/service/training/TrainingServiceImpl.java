package ua.orlov.springcoregym.service.training;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dao.impl.training.TrainingDao;
import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;
import ua.orlov.springcoregym.dto.training.TraineeTrainingsRequest;
import ua.orlov.springcoregym.dto.training.TrainerTrainingRequest;
import ua.orlov.springcoregym.mapper.trainer.TrainerMapper;
import ua.orlov.springcoregym.model.ActionType;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.service.user.trainer.WorkloadService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Log4j2
@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDAO;

    private final WorkloadService workloadService;

    private final TrainerMapper trainerMapper;

    @Override
    public Training create(Training training) {
        Objects.requireNonNull(training, "Training can't be null");
        Objects.requireNonNull(training.getTrainee(), "Training.trainee can't be null");
        Objects.requireNonNull(training.getTrainer(), "Training.trainer can't be null");
        Objects.requireNonNull(training.getTrainingName(), "Training.trainingName can't be null");
        Objects.requireNonNull(training.getTrainingType(), "Training.trainingType can't be null");
        Objects.requireNonNull(training.getTrainingDate(), "Training.trainingDate can't be null");
        Objects.requireNonNull(training.getTrainingDuration(), "Training.trainingDuration can't be null");

        Training createdTraining = trainingDAO.create(training);
        TrainerWorkload trainerWorkload = trainerMapper.trainerToTrainerWorkload(
                createdTraining.getTrainer(), createdTraining, ActionType.ADD
        );

        log.error("result of work = " + workloadService.changeWorkload(trainerWorkload));

        return createdTraining;
    }

    @Override
    public Training select(Long id) {
        return trainingDAO.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Training not found with id = " + id));
    }

    @Override
    public List<Training> getAll() {
        return trainingDAO.getAll();
    }

    @Override
    public List<Training> getTrainingsByCriteria(TraineeTrainingsRequest request) {
        return trainingDAO.getTrainingsByCriteria(request);
    }

    @Override
    public List<Training> getTrainingsByCriteria(TrainerTrainingRequest request) {
        return trainingDAO.getTrainingsByCriteria(request);
    }
}
