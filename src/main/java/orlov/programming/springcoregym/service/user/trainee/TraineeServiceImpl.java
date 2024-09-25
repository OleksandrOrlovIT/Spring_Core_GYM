package orlov.programming.springcoregym.service.user.trainee;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
public class TraineeServiceImpl implements TraineeService {

    private static final String TRAINEE_NULL_MESSAGE="Trainee can't be null";

    private final TraineeDao traineeDAO;

    private final PasswordGenerator passwordGenerator;

    public TraineeServiceImpl(TraineeDao traineeDAO, PasswordGenerator passwordGenerator) {
        this.traineeDAO = traineeDAO;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public void deleteByUsername(String userName) {
        traineeDAO.deleteByUsername(userName);
    }

    @Override
    public Trainee update(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        Trainee foundTrainee = select(trainee.getId());

        if(trainee.getPassword() == null || trainee.getPassword().length() != 10){
            trainee.setPassword(passwordGenerator.generatePassword());
        }

        if(foundTrainee.getIsActive() != trainee.getIsActive()){
            throw new IllegalArgumentException("IsActive field can't be changed in update");
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
    public Trainee select(Long id) {
        Trainee foundTrainee = traineeDAO.findById(id);

        if(foundTrainee == null){
            throw new NoSuchElementException("Trainee not found with id = " + id);
        }

        return foundTrainee;
    }

    private void checkAvailableUserName(Trainee trainee) {
        for(Trainee obj : traineeDAO.findAll()) {
            if(Objects.equals(obj.getUsername(), trainee.getUsername())) {
                trainee.setUsername(trainee.getUsername() + trainee.getId() + UUID.randomUUID());
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

    @Override
    public boolean userNameMatchPassword(String username, String password) {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername(username);

        if(foundTrainee.isEmpty()){
            throw new IllegalArgumentException("Trainee not found " + username);
        }

        return foundTrainee.get().getPassword().equals(password);
    }

    @Override
    public Trainee changePassword(Trainee trainee, String newPassword) {
        Trainee foundTrainee = select(trainee.getId());

        if(!foundTrainee.getPassword().equals(trainee.getPassword())){
            throw new IllegalArgumentException("Wrong password for trainee " + trainee.getUsername());
        }

        foundTrainee.setPassword(newPassword);

        return traineeDAO.update(foundTrainee);
    }

    @Override
    public Trainee activateTrainee(Long traineeId) {
        Trainee foundTrainee = select(traineeId);

        if(foundTrainee.getIsActive()){
            throw new IllegalArgumentException("Trainee is already active " + foundTrainee);
        }

        foundTrainee.setIsActive(true);

        return traineeDAO.update(foundTrainee);
    }

    @Override
    public Trainee deactivateTrainee(Long traineeId) {
        Trainee foundTrainee = select(traineeId);

        if(!foundTrainee.getIsActive()){
            throw new IllegalArgumentException("Trainee is already deactivated " + foundTrainee);
        }

        foundTrainee.setIsActive(true);

        return traineeDAO.update(foundTrainee);
    }

    @Override
    public List<Training> getTrainingsByDate(LocalDate startDate, LocalDate endDate, String userName) {
        return traineeDAO.getTrainingsByDateAndUsername(startDate, endDate, userName);
    }
}
