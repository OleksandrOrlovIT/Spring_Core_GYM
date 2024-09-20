package orlov.programming.springcoregym.dao.impl.user.trainee;

import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.impl.user.AbstractUserDao;
import orlov.programming.springcoregym.model.user.Trainee;

@Log4j2
@Repository
public class TraineeDaoImpl extends AbstractUserDao<Trainee> implements TraineeDao {

    private static final String TRAINEE_NULL_ERROR = "Trainee can't be null";

    @Autowired
    public TraineeDaoImpl(EntityManager entityManager) {
        super(entityManager, Trainee.class);
    }


    @Override
    protected String getNullEntityErrorMessage() {
        return TRAINEE_NULL_ERROR;
    }

    @Override
    protected void checkEntityIdIsNull(Long id) {
        if (id == null) {
            IllegalArgumentException e = new IllegalArgumentException("Trainee's id can't be null");
            log.error(e);
            throw e;
        }
    }

    @Override
    protected Long getId(Trainee trainee) {
        return trainee.getId();
    }

    @Override
    protected void setId(Trainee trainee, Long id) {
        trainee.setId(id);
    }

    @Override
    protected String getUsername(Trainee trainee) {
        return trainee.getUsername();
    }
}
