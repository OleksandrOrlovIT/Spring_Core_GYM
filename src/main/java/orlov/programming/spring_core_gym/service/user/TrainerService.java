package orlov.programming.spring_core_gym.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.service.UpdatableService;

@Service
public class TrainerService implements UpdatableService<Trainer> {

    @Autowired
    private DAO<Trainer> trainerDAO;

    @Override
    public Trainer update(Trainer trainer) {
        return trainerDAO.update(trainer);
    }

    @Override
    public Trainer create(Trainer trainer) {
        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer select(Trainer trainer) {
        return trainerDAO.findByObject(trainer);
    }
}