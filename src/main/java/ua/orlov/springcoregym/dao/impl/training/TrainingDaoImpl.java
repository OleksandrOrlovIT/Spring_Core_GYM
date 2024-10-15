package ua.orlov.springcoregym.dao.impl.training;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import ua.orlov.springcoregym.dao.AbstractDao;
import ua.orlov.springcoregym.model.training.Training;


@Log4j2
@Repository
public class TrainingDaoImpl extends AbstractDao<Training, Long> implements TrainingDao {

    @Override
    protected Class<Training> getEntityClass() {
        return Training.class;
    }
}
