package orlov.programming.springcoregym.service.user.trainer;

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
@AllArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDAO;

    private final PasswordGenerator passwordGenerator;

    private final TraineeDao traineeDao;

    @Override
    public Trainer update(Trainer trainer) {
        trainer.setUsername(constructTrainerUsername(trainer));

        checkAvailableUserName(trainer);

        Trainer foundTrainer = select(trainer.getId());

        if(trainer.getPassword() == null || trainer.getPassword().length() != 10){
            trainer.setPassword(passwordGenerator.generatePassword());
        }

        Objects.requireNonNull(trainer.getIsActive(), "Trainer's isActive field can't be null");

        if(foundTrainer.getIsActive() != trainer.getIsActive()){
            throw new IllegalArgumentException("IsActive field can't be changed in update");
        }

        return trainerDAO.update(trainer);
    }

    @Override
    public Trainer create(Trainer trainer) {
        trainer.setUsername(constructTrainerUsername(trainer));

        checkAvailableUserName(trainer);

        if(trainer.getPassword() == null || trainer.getPassword().length() != 10){
            trainer.setPassword(passwordGenerator.generatePassword());
        }

        Objects.requireNonNull(trainer.getIsActive(), "Trainer's isActive field can't be null");

        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer select(Long id) {
        Optional<Trainer> foundTrainer = trainerDAO.findById(id);

        if(foundTrainer.isEmpty()){
            throw new NoSuchElementException("Trainer not found with id = " + id);
        }

        return foundTrainer.get();
    }

    private void checkAvailableUserName(Trainer trainer) {
        Optional<Trainer> optionalTrainer = trainerDAO.findByUsername(trainer.getUsername());

        if(optionalTrainer.isPresent()){
            if(!optionalTrainer.get().getId().equals(trainer.getId())) {
                trainer.setUsername(trainer.getUsername() + UUID.randomUUID());
            } else {
                trainer.setUsername(optionalTrainer.get().getUsername());
            }
        }
    }

    private String constructTrainerUsername(Trainer trainer){
        checkFirstLastNames(trainer);

        return trainer.getFirstName() + "." + trainer.getLastName();
    }

    private void checkFirstLastNames(Trainer trainer){
        Objects.requireNonNull(trainer, "Trainer can't be null");
        Objects.requireNonNull(trainer.getFirstName(), "Trainer's firstName can't be null");
        Objects.requireNonNull(trainer.getLastName(), "Trainer's lastName can't be null");
    }

    @Override
    public boolean userNameMatchPassword(String username, String password) {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername(username);

        if(foundTrainer.isEmpty()){
            throw new IllegalArgumentException("Trainer not found " + username);
        }

        if(password != null){
            return password.equals(foundTrainer.get().getPassword());
        }

        return false;
    }

    @Override
    public Trainer changePassword(Trainer trainer, String newPassword) {
        Trainer foundTrainer = select(trainer.getId());

        if(!foundTrainer.getPassword().equals(trainer.getPassword())){
            throw new IllegalArgumentException("Wrong password for trainer " + trainer.getUsername());
        }

        foundTrainer.setPassword(newPassword);

        return trainerDAO.update(foundTrainer);
    }

    @Override
    public Trainer activateTrainer(Long trainerId) {
        Trainer foundTrainer = select(trainerId);

        if(foundTrainer.getIsActive()){
            throw new IllegalArgumentException("Trainer is already active " + foundTrainer);
        }

        foundTrainer.setIsActive(true);

        return trainerDAO.update(foundTrainer);
    }

    @Override
    public Trainer deactivateTrainer(Long trainerId) {
        Trainer foundTrainer = select(trainerId);

        if(!foundTrainer.getIsActive()){
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
    public List<Trainer> getTrainersWithoutPassedTrainee(String traineeUsername) {
        Optional<Trainee> trainee = traineeDao.findByUsername(traineeUsername);

        if(trainee.isEmpty()){
            throw new IllegalArgumentException("Trainee not found " + traineeUsername);
        }

        return trainerDAO.getTrainersWithoutPassedTrainee(trainee.get());
    }

    @Override
    public List<Trainer> findAll() {
        return trainerDAO.findAll();
    }

    @Override
    public Trainer authenticateTrainer(String userName, String password) {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername(userName);

        if(foundTrainer.isEmpty()){
            throw new IllegalArgumentException("Trainer not found " + userName);
        }

        if(!foundTrainer.get().getPassword().equals(password)){
            throw new IllegalArgumentException("Wrong password for trainer " + userName);
        }

        return foundTrainer.get();
    }

    @Override
    public Trainer findByUsername(String trainerUserName) {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername(trainerUserName);

        if(foundTrainer.isEmpty()){
            throw new IllegalArgumentException("Trainer not found " + trainerUserName);
        }

        return foundTrainer.get();
    }
}
