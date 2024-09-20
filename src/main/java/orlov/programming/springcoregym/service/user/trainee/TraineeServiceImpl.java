package orlov.programming.springcoregym.service.user.trainee;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.DaoUsernameFindable;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class TraineeServiceImpl implements TraineeService {

    private static final String TRAINEE_NULL_MESSAGE="Trainee can't be null";

    private final DaoUsernameFindable<Trainee> traineeDAO;

    private final PasswordGenerator passwordGenerator;

    public TraineeServiceImpl(DaoUsernameFindable<Trainee> traineeDAO, PasswordGenerator passwordGenerator) {
        this.traineeDAO = traineeDAO;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public void delete(Trainee trainee) {
        traineeDAO.delete(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        select(trainee);

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
        Objects.requireNonNull(trainee, TRAINEE_NULL_MESSAGE);

        Optional<Trainee> foundTrainee = traineeDAO.findByUsername(trainee.getUsername());

        if(foundTrainee.isEmpty()){
            throw new NoSuchElementException("Trainee not found " + trainee);
        }

        return foundTrainee.get();
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
