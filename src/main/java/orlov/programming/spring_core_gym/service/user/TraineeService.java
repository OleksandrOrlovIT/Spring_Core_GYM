package orlov.programming.spring_core_gym.service.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.spring_core_gym.dao.impl.user.DAOUsernameFindable;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.service.CRUDService;
import orlov.programming.spring_core_gym.util.PasswordGenerator;

import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
public class TraineeService implements CRUDService<Trainee> {

    @Autowired
    private DAOUsernameFindable<Trainee> traineeDAO;

    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public void delete(Trainee trainee) {
        traineeDAO.delete(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        if(select(trainee) == null){
            IllegalArgumentException e = new IllegalArgumentException("Trainee not found for " + trainee);
            log.error(e);
            throw e;
        }

        if(trainee.getPassword() == null || trainee.getPassword().length() != 10){
            trainee.setPassword(passwordGenerator.generatePassword());
        }

        return traineeDAO.update(trainee);
    }

    @Override
    public Trainee create(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        checkAvailableUserName(trainee);

        if(trainee.getPassword() == null || trainee.getPassword().length() != 10){
            trainee.setPassword(passwordGenerator.generatePassword());
        }

        return traineeDAO.create(trainee);
    }

    @Override
    public Trainee select(Trainee trainee) {
        Objects.requireNonNull(trainee, "Trainee can't be null");

        return traineeDAO.findByUsername(trainee.getUsername());
    }

    private void checkAvailableUserName(Trainee trainee) {
        for(Trainee obj : traineeDAO.findAll()) {
            if(Objects.equals(obj.getUsername(), trainee.getUsername())) {
                trainee.setUsername(trainee.getUsername() + trainee.getUserId() + UUID.randomUUID());
            }
        }
    }

    private String constructTraineeUsername(Trainee trainee){
        checkFirstLastNames(trainee);

        return trainee.getFirstName() + "." + trainee.getLastName();
    }

    private void checkFirstLastNames(Trainee trainee){
        Objects.requireNonNull(trainee, "Trainee can't be null");
        Objects.requireNonNull(trainee.getFirstName(), "Trainee's firstName can't be null");
        Objects.requireNonNull(trainee.getLastName(), "Trainee's lastName can't be null");
    }
}