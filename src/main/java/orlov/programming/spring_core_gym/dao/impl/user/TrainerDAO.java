package orlov.programming.spring_core_gym.dao.impl.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Repository
public class TrainerDAO implements DAOUsernameFindable<Trainer> {

    private static final String TRAINER_NULL_ERROR = "Trainer can't be null";

    private final Map<Long, Trainer> trainerHashMap;
    private static long nextId;

    @Autowired
    public TrainerDAO(Storage<Long, Trainer> storage) {
        log.info("Creating TrainerDAO");
        this.trainerHashMap = storage.getStorage();
        nextId = storage.getLastKey();
    }

    @Override
    public Trainer create(Trainer trainer) {
        Objects.requireNonNull(trainer, TRAINER_NULL_ERROR);
        if(trainer.getUserId() != null){
            throw new IllegalArgumentException("Trainer's id has to be null");
        }

        trainer.setUserId(nextId);
        nextId++;

        Trainer savedTrainer = trainerHashMap.put(trainer.getUserId(), trainer);
        log.info("Created new Trainer = {}", savedTrainer);
        return savedTrainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        Objects.requireNonNull(trainer, TRAINER_NULL_ERROR);
        checkUserIdEqualsNull(trainer.getUserId());

        if(findByObject(trainer) == null){
            IllegalArgumentException e = new IllegalArgumentException("Trainer does not exist");
            log.error(e);
            throw e;
        }

        Trainer updatedTrainer = trainerHashMap.put(trainer.getUserId(), trainer);
        log.info("Updating Trainer = {}", updatedTrainer);
        return updatedTrainer;
    }

    @Override
    public void delete(Trainer trainer) {
        log.info("Deleting Trainer = {}", trainer);

        checkUserIdEqualsNull(trainer.getUserId());

        trainerHashMap.remove(trainer.getUserId());
    }

    @Override
    public List<Trainer> findAll() {
        return trainerHashMap.values().stream().toList();
    }

    @Override
    public Trainer findByObject(Trainer trainer) {
        Objects.requireNonNull(trainer, TRAINER_NULL_ERROR);
        checkUserIdEqualsNull(trainer.getUserId());

        return trainerHashMap.get(trainer.getUserId());
    }

    @Override
    public Trainer findByUsername(String username) {
        for (Trainer trainer : trainerHashMap.values()) {
            if (trainer.getUsername().equals(username)) {
                return trainer;
            }
        }

        return null;
    }

    private void checkUserIdEqualsNull(Long id){
        if(id == null){
            IllegalArgumentException e = new IllegalArgumentException("Trainer's id can't be null");
            log.error(e);
            throw e;
        }
    }
}