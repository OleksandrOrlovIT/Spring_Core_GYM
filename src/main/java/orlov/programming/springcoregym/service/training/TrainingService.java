package orlov.programming.springcoregym.service.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.DAOSelectableCreatable;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.service.CSService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TrainingService implements CSService<Training> {

    private final DAOSelectableCreatable<Training> trainingDAO;

    public TrainingService(DAOSelectableCreatable<Training> trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training create(Training training) {
        return trainingDAO.create(training);
    }

    @Override
    public Training select(Training training) {
        Optional<Training> foundTraining = trainingDAO.findByObject(training);
        if (foundTraining.isEmpty()) {
            throw new NoSuchElementException("Training not found " + training);
        }

        return foundTraining.get();
    }
}
