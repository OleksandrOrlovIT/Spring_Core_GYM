package orlov.programming.springcoregym.dao.impl.training;

import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.model.training.TrainingType;

@Repository
public class TrainingTypeDaoImpl extends AbstractDao<TrainingType, Long> implements TrainingTypeDao {
    @Override
    protected Class<TrainingType> getEntityClass() {
        return TrainingType.class;
    }
}
