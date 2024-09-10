package orlov.programming.springcoregym.dao.impl.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.storage.Storage;

@Log4j2
@Repository
public class TrainerDAO extends AbstractUserDAO<Trainer> {

    private static final String TRAINER_NULL_ERROR = "Trainer can't be null";

    @Autowired
    public TrainerDAO(Storage storage) {
        super(storage, Trainer.class);
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
    protected Long getUserId(Trainer trainer) {
        return trainer.getUserId();
    }

    @Override
    protected void setUserId(Trainer trainer, Long id) {
        trainer.setUserId(id);
    }

    @Override
    protected String getUsername(Trainer trainer) {
        return trainer.getUsername();
    }
}
