package orlov.programming.spring_core_gym.dao.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.storage.Storage;

import java.util.List;
import java.util.Map;

@Repository
public class TrainerDAO implements DAO<Trainer> {
    private final Map<Long, Trainer> trainerHashMap;
    private static long nextId;

    @Autowired
    public TrainerDAO(Storage<Long, Trainer> storage) {
        this.trainerHashMap = storage.getStorage();
        nextId = storage.getLastKey();
    }

    @Override
    public Trainer create(Trainer trainer) {
        if(trainer.getUserId() != null){
            throw new IllegalArgumentException("Trainer's id has to be null");
        }

        trainer.setUserId(nextId);
        nextId++;

        return trainerHashMap.put(trainer.getUserId(), trainer);
    }

    @Override
    public Trainer update(Trainer trainer) {
        checkUserIdEqualsNull(trainer.getUserId());

        if(findByObject(trainer) == null){
            throw new IllegalArgumentException("Trainer does not exist");
        }

        return trainerHashMap.put(trainer.getUserId(), trainer);
    }

    @Override
    public void delete(Trainer trainer) {
        checkUserIdEqualsNull(trainer.getUserId());

        trainerHashMap.remove(trainer.getUserId());
    }

    @Override
    public List<Trainer> findAll() {
        return trainerHashMap.values().stream().toList();
    }

    @Override
    public Trainer findByObject(Trainer trainer) {
        checkUserIdEqualsNull(trainer.getUserId());

        return trainerHashMap.get(trainer.getUserId());
    }

    private void checkUserIdEqualsNull(Long id){
        if(id == null){
            throw new IllegalArgumentException("Trainer's id can't be null");
        }
    }
}