package orlov.programming.spring_core_gym.dao.user;

import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.user.Trainer;

import java.util.HashMap;
import java.util.List;

@Repository
public class TrainerDAO implements DAO<Trainer> {
    private static final HashMap<Long, Trainer> trainerHashMap;
    private static long nextId;

    static {
        trainerHashMap = new HashMap<>();
        nextId = 1L;
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