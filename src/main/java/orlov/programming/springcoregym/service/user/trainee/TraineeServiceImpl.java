package orlov.programming.springcoregym.service.user.trainee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.impl.user.trainee.TraineeDao;
import orlov.programming.springcoregym.dao.impl.user.trainer.TrainerDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDAO;

    private final TrainerDao trainerDAO;

    private final PasswordGenerator passwordGenerator;

    @Override
    public void deleteByUsername(String userName) {
        traineeDAO.deleteByUsername(userName);
    }

    @Override
    public Trainee update(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        checkAvailableUserName(trainee);

        Trainee foundTrainee = select(trainee.getId());

        if(trainee.getPassword() == null || trainee.getPassword().length() != 10){
            trainee.setPassword(passwordGenerator.generatePassword());
        }

        Objects.requireNonNull(trainee.getIsActive(), "Trainee's isActive field can't be null");
        if(foundTrainee.getIsActive() != trainee.getIsActive()){
            throw new IllegalArgumentException("IsActive field can't be changed in update");
        }

        trainee.setTrainings(foundTrainee.getTrainings());

        return traineeDAO.update(trainee);
    }

    @Override
    public Trainee create(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        checkAvailableUserName(trainee);

        Objects.requireNonNull(trainee.getIsActive(), "Trainee's isActive field can't be null");

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
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername(trainee.getUsername());

        if(foundTrainee.isPresent()){
            if(!foundTrainee.get().getId().equals(trainee.getId())) {
                trainee.setUsername(trainee.getUsername() + UUID.randomUUID());
            } else {
                trainee.setUsername(foundTrainee.get().getUsername());
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
    public List<Training> getTrainingsByDateTraineeNameTrainingType
            (LocalDate startDate, LocalDate endDate, String userName, String trainingTypename) {
        return traineeDAO.getTrainingsByDateUsernameTrainingType(startDate, endDate, userName, trainingTypename);
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDAO.findAll();
    }

    @Override
    public Trainee authenticateTrainee(String userName, String password) {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername(userName);

        if(foundTrainee.isEmpty()){
            throw new IllegalArgumentException("Trainee not found " + userName);
        }

        if(!foundTrainee.get().getPassword().equals(password)){
            throw new IllegalArgumentException("Wrong password for trainee " + userName);
        }

        return foundTrainee.get();
    }

    @Override
    public Trainee findByUsername(String traineeUsername) {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername(traineeUsername);

        if(foundTrainee.isEmpty()){
            throw new IllegalArgumentException("Trainee not found " + traineeUsername);
        }

        return foundTrainee.get();
    }

    @Transactional
    public void updateTraineeTrainers(Long traineeId, List<Long> trainerIds) {
        Trainee trainee = select(traineeId);

        List<Trainer> trainers = trainerDAO.findByIds(trainerIds);
        if (trainers.isEmpty()) {
            throw new EntityNotFoundException("No trainers found with the provided IDs");
        }

        trainee.setTrainers(trainers);

        for (Trainer trainer : trainers) {
            if(trainer.getTrainees() == null){
                trainer.setTrainees(new ArrayList<>());
            }

            if (!trainer.getTrainees().contains(trainee)) {
                trainer.getTrainees().add(trainee);
            }
        }

        traineeDAO.update(trainee);
    }
}
