package orlov.programming.spring_core_gym.dao.user;

import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.user.Trainee;

import java.util.HashMap;
import java.util.List;

@Repository
public class TraineeDAO implements DAO<Trainee> {

    private static final HashMap<Long, Trainee> traineeHashMap;
    private static long nextId;

    static {
        traineeHashMap = new HashMap<>();
        nextId = 1L;
    }

    @Override
    public Trainee create(Trainee trainee) {
        if(trainee.getUserId() != null){
            throw new IllegalArgumentException("Trainee's id has to be null");
        }

        trainee.setUserId(nextId);
        nextId++;

        return traineeHashMap.put(trainee.getUserId(), trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        checkUserIdEqualsNull(trainee.getUserId());

        if(findByObject(trainee) == null){
            throw new IllegalArgumentException("Trainee does not exist");
        }

        return traineeHashMap.put(trainee.getUserId(), trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        checkUserIdEqualsNull(trainee.getUserId());

        traineeHashMap.remove(trainee.getUserId());
    }

    @Override
    public List<Trainee> findAll() {
        return traineeHashMap.values().stream().toList();
    }

    @Override
    public Trainee findByObject(Trainee trainee) {
        checkUserIdEqualsNull(trainee.getUserId());

        return traineeHashMap.get(trainee.getUserId());
    }

    private void checkUserIdEqualsNull(Long id){
        if(id == null){
            throw new IllegalArgumentException("Trainee's id can't be null");
        }
    }
}