package orlov.programming.springcoregym.service.training;

import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
import orlov.programming.springcoregym.model.training.Training;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDAO;

    public TrainingServiceImpl(TrainingDao trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training create(Training training) {
        Objects.requireNonNull(training, "Training can't be null");
        Objects.requireNonNull(training.getTrainee(), "Training.trainee can't be null");
        Objects.requireNonNull(training.getTrainer(), "Training.trainer can't be null");
        Objects.requireNonNull(training.getTrainingName(), "Training.trainingName can't be null");
        Objects.requireNonNull(training.getTrainingType(), "Training.trainingType can't be null");
        Objects.requireNonNull(training.getTrainingDate(), "Training.trainingDate can't be null");
        Objects.requireNonNull(training.getTrainingDuration(), "Training.trainingDuration can't be null");

        return trainingDAO.create(training);
    }

    @Override
    public Training select(Long id) {
        Training foundTraining = trainingDAO.findById(id);
        if (foundTraining == null) {
            throw new NoSuchElementException("Training not found with id " + id);
        }

        return foundTraining;
    }

    @Override
    public List<Training> findAll() {
        return trainingDAO.findAll();
    }
}
