package orlov.programming.spring_core_gym.service.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import orlov.programming.spring_core_gym.dao.impl.user.DAOUsernameFindable;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.service.UpdatableService;
import orlov.programming.spring_core_gym.util.PasswordGenerator;

import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
public class TrainerService implements UpdatableService<Trainer> {

    @Autowired
    private DAOUsernameFindable<Trainer> trainerDAO;

    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public Trainer update(Trainer trainer) {
        trainer.setUsername(constructTrainerUsername(trainer));

        if(select(trainer) == null){
            IllegalArgumentException e = new IllegalArgumentException("Trainer not found for " + trainer);
            log.error(e);
            throw e;
        }

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
        return trainerDAO.findByUsername(trainer.getUsername());
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