package orlov.programming.spring_core_gym.service.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.service.CSService;

@Service
public class TrainingService implements CSService<Training> {

    @Autowired
    private DAO<Training> trainingDAO;

    @Override
    public Training create(Training training) {
        return trainingDAO.create(training);
    }

    @Override
    public Training select(Training training) {
        return trainingDAO.findByObject(training);
    }
}