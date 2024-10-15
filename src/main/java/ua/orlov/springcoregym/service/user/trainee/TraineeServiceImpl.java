package ua.orlov.springcoregym.service.user.trainee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dao.impl.user.UserDao;
import ua.orlov.springcoregym.dao.impl.user.trainee.TraineeDao;
import ua.orlov.springcoregym.dao.impl.user.trainer.TrainerDao;
import ua.orlov.springcoregym.dto.trainee.TraineeTrainingDTO;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.util.PasswordGenerator;

import java.util.*;

@Log4j2
@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeDao traineeDAO;

    private final TrainerDao trainerDAO;

    private final PasswordGenerator passwordGenerator;

    private final UserDao userDao;

    @Override
    public void deleteByUsername(String userName) {
        traineeDAO.deleteByUsername(userName);
    }

    @Override
    public Trainee update(Trainee trainee) {
        checkNames(trainee);

        Trainee foundTrainee = getByUsername(trainee.getUsername());
        trainee.setId(foundTrainee.getId());

        if (trainee.getPassword() == null || trainee.getPassword().length() != passwordGenerator.getPasswordLength()) {
            trainee.setPassword(passwordGenerator.generatePassword());
        }

        Objects.requireNonNull(trainee.getIsActive(), "Trainee's isActive field can't be null");
        if (foundTrainee.getIsActive() != trainee.getIsActive()) {
            throw new IllegalArgumentException("IsActive field can't be changed in update");
        }

        trainee.setTrainings(foundTrainee.getTrainings());

        trainee = traineeDAO.update(trainee);

        return getByUserNameWithTrainers(trainee.getUsername());
    }

    @Override
    public Trainee create(Trainee trainee) {
        trainee.setUsername(constructTraineeUsername(trainee));

        checkAvailableUserName(trainee);

        if(trainee.getIsActive() == null){
            trainee.setIsActive(false);
        }

        if (trainee.getPassword() == null || trainee.getPassword().length() != passwordGenerator.getPasswordLength()) {
            trainee.setPassword(passwordGenerator.generatePassword());
        }

        return traineeDAO.create(trainee);
    }

    @Override
    public Trainee select(Long id) {
        return traineeDAO.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Trainee not found with id = " + id));
    }

    private void checkAvailableUserName(Trainee trainee) {
        userDao.getByUsername(trainee.getUsername())
                .ifPresent(foundTrainee -> {
                    if (!foundTrainee.getId().equals(trainee.getId())) {
                        trainee.setUsername(trainee.getUsername() + UUID.randomUUID());
                    } else {
                        trainee.setUsername(foundTrainee.getUsername());
                    }
                });
    }

    private String constructTraineeUsername(Trainee trainee) {
        checkFirstLastNames(trainee);

        return trainee.getFirstName() + "." + trainee.getLastName();
    }

    private void checkFirstLastNames(Trainee trainee) {
        Objects.requireNonNull(trainee, "Trainee can't be null");
        Objects.requireNonNull(trainee.getFirstName(), "Trainee's firstName can't be null");
        Objects.requireNonNull(trainee.getLastName(), "Trainee's lastName can't be null");
    }

    private void checkNames(Trainee trainee){
        checkFirstLastNames(trainee);
        Objects.requireNonNull(trainee.getUsername(), "Trainee.username can't be null");
    }

    @Override
    public boolean isUserNameMatchPassword(String username, String password) {
        Trainee foundTrainee = traineeDAO.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found " + username));

        if (password != null) {
            return password.equals(foundTrainee.getPassword());
        }

        return false;
    }

    @Override
    public Trainee changePassword(Trainee trainee, String newPassword) {
        Trainee foundTrainee = select(trainee.getId());

        if (!foundTrainee.getPassword().equals(trainee.getPassword())) {
            throw new IllegalArgumentException("Wrong password for trainee " + trainee.getUsername());
        }

        foundTrainee.setPassword(newPassword);

        return traineeDAO.update(foundTrainee);
    }

    @Override
    public Trainee activateTrainee(Long traineeId) {
        Trainee foundTrainee = select(traineeId);

        if (foundTrainee.getIsActive()) {
            throw new IllegalArgumentException("Trainee is already active " + foundTrainee);
        }

        foundTrainee.setIsActive(true);

        return traineeDAO.update(foundTrainee);
    }

    @Override
    public Trainee deactivateTrainee(Long traineeId) {
        Trainee foundTrainee = select(traineeId);

        if (!foundTrainee.getIsActive()) {
            throw new IllegalArgumentException("Trainee is already deactivated " + foundTrainee);
        }

        foundTrainee.setIsActive(false);

        return traineeDAO.update(foundTrainee);
    }

    @Override
    public List<Training> getTrainingsByTraineeTrainingDTO(TraineeTrainingDTO traineeTrainingDTO) {
        return traineeDAO.getTrainingsByTraineeTrainingDTO(traineeTrainingDTO);
    }

    @Override
    public List<Trainee> getAll() {
        return traineeDAO.getAll();
    }

    @Override
    public Trainee authenticateTrainee(String userName, String password) {
        Trainee foundTrainee = traineeDAO.getByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found " + userName));

        if (!foundTrainee.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password for trainee " + userName);
        }

        return foundTrainee;
    }

    @Override
    public Trainee getByUsername(String traineeUsername) {
        return traineeDAO.getByUsername(traineeUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainee not found " + traineeUsername));
    }

    @Transactional
    public List<Trainer> updateTraineeTrainers(Long traineeId, List<Long> trainerIds) {
        Trainee trainee = select(traineeId);

        List<Trainer> trainers = trainerDAO.getByIds(trainerIds);
        if (trainers.isEmpty()) {
            throw new EntityNotFoundException("No trainers found with the provided IDs");
        }

        trainee.setTrainers(trainers);

        for (Trainer trainer : trainers) {
            if (trainer.getTrainees() == null) {
                trainer.setTrainees(new ArrayList<>());
            }

            if (!trainer.getTrainees().contains(trainee)) {
                trainer.getTrainees().add(trainee);
            }
        }

        trainee = traineeDAO.update(trainee);

        return traineeDAO.getTrainersByTraineeUsername(trainee.getUsername());
    }

    @Transactional
    public List<Trainer> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = getByUsername(traineeUsername);

        List<Trainer> trainers = trainerDAO.getByUsernames(trainerUsernames);
        if (trainers.isEmpty()) {
            throw new EntityNotFoundException("No trainers found with the provided userNames");
        }

        trainee.setTrainers(trainers);

        for (Trainer trainer : trainers) {
            if (trainer.getTrainees() == null) {
                trainer.setTrainees(new ArrayList<>());
            }

            if (!trainer.getTrainees().contains(trainee)) {
                trainer.getTrainees().add(trainee);
            }
        }

        trainee = traineeDAO.update(trainee);

        return traineeDAO.getTrainersByTraineeUsername(trainee.getUsername());
    }

    @Override
    public Trainee getByUserNameWithTrainers(String traineeUsername) {
        Trainee trainee = getByUsername(traineeUsername);

        trainee.setTrainers(traineeDAO.getTrainersByTraineeUsername(traineeUsername));

        return trainee;
    }

    @Override
    public void activateDeactivateTrainee(String traineeUsername, boolean isActive) {
        Trainee trainee = getByUsername(traineeUsername);

        if(isActive){
            deactivateTrainee(trainee.getId());
        } else {
            activateTrainee(trainee.getId());
        }
    }
}