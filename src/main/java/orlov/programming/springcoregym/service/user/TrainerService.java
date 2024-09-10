package orlov.programming.springcoregym.service.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.impl.user.DAOUsernameFindable;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.UpdatableService;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class TrainerService implements UpdatableService<Trainer> {

    private final DAOUsernameFindable<Trainer> trainerDAO;

    private final PasswordGenerator passwordGenerator;

    @Autowired
    public TrainerService(DAOUsernameFindable<Trainer> trainerDAO, PasswordGenerator passwordGenerator) {
        this.trainerDAO = trainerDAO;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public Trainer update(Trainer trainer) {
        trainer.setUsername(constructTrainerUsername(trainer));

        select(trainer);

        if(trainer.getPassword() == null || trainer.getPassword().length() != 10){
            trainer.setPassword(passwordGenerator.generatePassword());
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

        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer select(Trainer trainer) {
        Objects.requireNonNull(trainer, "Trainer can't be null");
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername(trainer.getUsername());

        if(foundTrainer.isEmpty()){
            throw new NoSuchElementException("Trainer not found " + trainer);
        }

        return foundTrainer.get();
    }

    private void checkAvailableUserName(Trainer trainer) {
        for(Trainer obj : trainerDAO.findAll()) {
            if(Objects.equals(obj.getUsername(), trainer.getUsername())) {
                trainer.setUsername(trainer.getUsername() + trainer.getUserId() + UUID.randomUUID());
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
}
