package orlov.programming.springcoregym.dao.impl.user.trainer;

import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.impl.user.AbstractUserDao;
import orlov.programming.springcoregym.model.user.Trainer;

@Log4j2
@Repository
public class TrainerDaoImpl extends AbstractUserDao<Trainer> implements TrainerDao {

    private static final String TRAINER_NULL_ERROR = "Trainer can't be null";

    @Autowired
    public TrainerDaoImpl(EntityManager entityManager) {
        super(entityManager, Trainer.class);
    }

    @Override
    protected String getNullEntityErrorMessage() {
        return TRAINER_NULL_ERROR;
    }

    @Override
    protected void checkEntityIdIsNull(Long id) {
        if (id == null) {
            IllegalArgumentException e = new IllegalArgumentException("Trainer's id can't be null");
            log.error(e);
            throw e;
        }
    }

    @Override
    protected Long getId(Trainer trainer) {
        return trainer.getId();
    }

    @Override
    protected void setId(Trainer trainer, Long id) {
        trainer.setId(id);
    }

    @Override
    protected String getUsername(Trainer trainer) {
        return trainer.getUsername();
    }
}
