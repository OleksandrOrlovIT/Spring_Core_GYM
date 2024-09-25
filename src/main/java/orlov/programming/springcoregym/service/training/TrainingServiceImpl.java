package orlov.programming.springcoregym.service.training;

import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.impl.training.TrainingDao;
import orlov.programming.springcoregym.model.training.Training;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDAO;

    public TrainingServiceImpl(TrainingDao trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training create(Training training) {
        return trainingDAO.create(training);
    }

    @Override
    public Training select(Long id) {
//        Optional<Training> foundTraining = trainingDAO.findByObject(training);
//        if (foundTraining.isEmpty()) {
//            throw new NoSuchElementException("Training not found " + training);
//        }
//
//        return foundTraining.get();e
        return null;
    }
}
