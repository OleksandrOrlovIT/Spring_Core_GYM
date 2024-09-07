package orlov.programming.spring_core_gym.dao.impl.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.spring_core_gym.dao.DAO;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Repository
public class TraineeDAO implements DAO<Trainee> {

    private static final String TRAINEE_NULL_ERROR = "Trainee can't be null";

    private final Map<Long, Trainee> traineeHashMap;
    private static long nextId;

    @Autowired
    public TraineeDAO(Storage<Long, Trainee> storage) {
        log.info("Creating TraineeDAO");
        this.traineeHashMap = storage.getStorage();
        nextId = storage.getLastKey();
    }

    @Override
    public Trainee create(Trainee trainee) {
        Objects.requireNonNull(trainee, TRAINEE_NULL_ERROR);
        if(trainee.getUserId() != null){
            throw new IllegalArgumentException("Trainee's id has to be null");
        }

        trainee.setUserId(nextId);
        nextId++;

        Trainee savedTrainee = traineeHashMap.put(trainee.getUserId(), trainee);
        log.info("Created new Trainee = {}", savedTrainee);

        return savedTrainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        Objects.requireNonNull(trainee, TRAINEE_NULL_ERROR);
        isUserIdEqualsNull(trainee.getUserId());

        if(findByObject(trainee) == null){
            IllegalArgumentException e = new IllegalArgumentException("Trainee does not exist");
            log.error(e);
            throw e;
        }

        Trainee updatedTrainee = traineeHashMap.put(trainee.getUserId(), trainee);
        log.info("Updating Trainee = {}", updatedTrainee);
        return updatedTrainee;
    }

    @Override
    public void delete(Trainee trainee) {
        log.info("Deleting Trainee = {}", trainee);

        isUserIdEqualsNull(trainee.getUserId());

        traineeHashMap.remove(trainee.getUserId());
    }

    @Override
    public List<Trainee> findAll() {
        return traineeHashMap.values().stream().toList();
    }

    @Override
    public Trainee findByObject(Trainee trainee) {
        Objects.requireNonNull(trainee, TRAINEE_NULL_ERROR);
        isUserIdEqualsNull(trainee.getUserId());

        return traineeHashMap.get(trainee.getUserId());
    }

    private void isUserIdEqualsNull(Long id){
        if(id == null){
            IllegalArgumentException e = new IllegalArgumentException("Trainee's id can't be null");
            log.error(e);
            throw e;
        }
    }
}