package ua.orlov.springcoregym.service.user.trainer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dao.impl.user.UserDao;
import ua.orlov.springcoregym.dao.impl.user.trainee.TraineeDao;
import ua.orlov.springcoregym.dao.impl.user.trainer.TrainerDao;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.util.PasswordGenerator;
import ua.orlov.springcoregym.util.model.Pageable;

import java.time.LocalDate;
import java.util.*;

@Log4j2
@AllArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDAO;

    private final PasswordGenerator passwordGenerator;

    private final TraineeDao traineeDAO;

    private final UserDao userDao;

    @Override
    public Trainer update(Trainer trainer) {
        Trainer foundTrainer = getByUsername(trainer.getUsername());
        trainer.setId(foundTrainer.getId());

        trainer.setUsername(constructTrainerUsername(trainer));

        checkAvailableUserName(trainer);

        if (trainer.getPassword() == null || trainer.getPassword().length() != passwordGenerator.getPasswordLength()) {
            trainer.setPassword(passwordGenerator.generatePassword());
        }

        Objects.requireNonNull(trainer.getIsActive(), "Trainer's isActive field can't be null");

        if (foundTrainer.getIsActive() != trainer.getIsActive()) {
            throw new IllegalArgumentException("IsActive field can't be changed in update");
        }

        trainer = trainerDAO.update(trainer);

        return getByUserNameWithTrainees(trainer.getUsername());
    }

    @Override
    public Trainer create(Trainer trainer) {
        trainer.setUsername(constructTrainerUsername(trainer));

        checkAvailableUserName(trainer);

        if (trainer.getPassword() == null || trainer.getPassword().length() != passwordGenerator.getPasswordLength()) {
            trainer.setPassword(passwordGenerator.generatePassword());
        }

        if(trainer.getIsActive() == null){
            trainer.setIsActive(false);
        }

        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer select(Long id) {
        return trainerDAO.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Trainer not found with id = " + id));
    }

    private void checkAvailableUserName(Trainer trainer) {
        userDao.getByUsername(trainer.getUsername())
                .ifPresent(foundTrainee -> {
                    if (!foundTrainee.getId().equals(trainer.getId())) {
                        trainer.setUsername(trainer.getUsername() + UUID.randomUUID());
                    } else {
                        trainer.setUsername(foundTrainee.getUsername());
                    }
                });
    }

    private String constructTrainerUsername(Trainer trainer) {
        checkFirstLastNames(trainer);

        return trainer.getFirstName() + "." + trainer.getLastName();
    }

    private void checkFirstLastNames(Trainer trainer) {
        Objects.requireNonNull(trainer, "Trainer can't be null");
        Objects.requireNonNull(trainer.getFirstName(), "Trainer's firstName can't be null");
        Objects.requireNonNull(trainer.getLastName(), "Trainer's lastName can't be null");
    }

    @Override
    public boolean isUserNameMatchPassword(String username, String password) {
        Trainer foundTrainer = trainerDAO.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found " + username));

        if (password != null) {
            return password.equals(foundTrainer.getPassword());
        }

        return false;
    }

    @Override
    public Trainer changePassword(Trainer trainer, String newPassword) {
        Trainer foundTrainer = select(trainer.getId());

        if (!foundTrainer.getPassword().equals(trainer.getPassword())) {
            throw new IllegalArgumentException("Wrong password for trainer " + trainer.getUsername());
        }

        foundTrainer.setPassword(newPassword);

        return trainerDAO.update(foundTrainer);
    }

    @Override
    public Trainer activateTrainer(Long trainerId) {
        Trainer foundTrainer = select(trainerId);

        if (foundTrainer.getIsActive()) {
            throw new IllegalArgumentException("Trainer is already active " + foundTrainer);
        }

        foundTrainer.setIsActive(true);

        return trainerDAO.update(foundTrainer);
    }

    @Override
    public Trainer deactivateTrainer(Long trainerId) {
        Trainer foundTrainer = select(trainerId);

        if (!foundTrainer.getIsActive()) {
            throw new IllegalArgumentException("Trainer is already deactivated " + foundTrainer);
        }

        foundTrainer.setIsActive(true);

        return trainerDAO.update(foundTrainer);
    }

    @Override
    public List<Training> getTrainingsByDate(LocalDate startDate, LocalDate endDate, String userName) {
        return trainerDAO.getTrainingsByDateAndUsername(startDate, endDate, userName);
    }

    @Override
    public List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername, Pageable pageable) {
        Trainee trainee = traineeDAO.getByUsername(traineeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found " + traineeUsername));

        return trainerDAO.getTrainersWithoutPassedTrainee(trainee, pageable);
    }

    @Override
    public List<Trainer> getAll() {
        return trainerDAO.getAll();
    }

    @Override
    public Trainer authenticateTrainer(String userName, String password) {
        Trainer foundTrainer = trainerDAO.getByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found " + userName));

        if (!foundTrainer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password for trainer " + userName);
        }

        return foundTrainer;
    }

    @Override
    public Trainer getByUsername(String trainerUserName) {
        return trainerDAO.getByUsername(trainerUserName)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found " + trainerUserName));
    }

    @Override
    public Trainer getByUserNameWithTrainees(String trainerUsername) {
        Trainer trainer = getByUsername(trainerUsername);

        trainer.setTrainees(trainerDAO.getTraineesByTrainerUsername(trainer.getUsername()));

        return trainer;
    }
}
