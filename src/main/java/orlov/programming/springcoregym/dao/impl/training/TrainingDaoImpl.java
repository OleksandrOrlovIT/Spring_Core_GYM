package orlov.programming.springcoregym.dao.impl.training;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.model.training.Training;


@Log4j2
@Repository
public class TrainingDaoImpl extends AbstractDao<Training, Long> implements TrainingDao {

    @Override
    protected Class<Training> getEntityClass() {
        return Training.class;
    }
}
