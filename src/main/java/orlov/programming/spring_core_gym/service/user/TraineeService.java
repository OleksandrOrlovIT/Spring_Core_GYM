package orlov.programming.spring_core_gym.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.service.CRUDService;

@Service
public class TraineeService implements CRUDService<Trainee> {

    @Autowired
    private DAO<Trainee> traineeDAO;

    @Override
    public void delete(Trainee trainee) {
        traineeDAO.delete(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        return traineeDAO.update(trainee);
    }

    @Override
    public Trainee create(Trainee trainee) {
        return traineeDAO.create(trainee);
    }

    @Override
    public Trainee select(Trainee trainee) {
        return traineeDAO.findByObject(trainee);
    }
}
