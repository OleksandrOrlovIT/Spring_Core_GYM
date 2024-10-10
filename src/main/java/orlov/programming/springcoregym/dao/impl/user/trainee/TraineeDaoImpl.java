package orlov.programming.springcoregym.dao.impl.user.trainee;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.dto.trainee.TraineeTrainingDTO;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class TraineeDaoImpl extends AbstractDao<Trainee, Long> implements TraineeDao {

    private static String FIND_BY_USERNAME_QUERY;
    private static String GET_TRAININGS_BY_DATE_USERNAME_TRAINING_TYPE_QUERY;
    private static String DELETE_BY_USERNAME_QUERY;

    public TraineeDaoImpl() {
        FIND_BY_USERNAME_QUERY = "SELECT t FROM " + getEntityClass().getSimpleName() + " t WHERE t.username = :username";
        GET_TRAININGS_BY_DATE_USERNAME_TRAINING_TYPE_QUERY = """
                SELECT tr FROM Training tr
                JOIN tr.trainee t
                JOIN tr.trainingType tt
                WHERE t.username = :username
                AND tt.trainingTypeName = :trainingType
                AND tr.trainingDate BETWEEN :startDate AND :endDate
                """;
        DELETE_BY_USERNAME_QUERY = "DELETE FROM " + getEntityClass().getSimpleName() + " t WHERE t.username = :username";
    }

    @Override
    protected Class<Trainee> getEntityClass() {
        return Trainee.class;
    }

    @Override
    public Optional<Trainee> getByUsername(String username) {
        try {
            TypedQuery<Trainee> query = getEntityManager().createQuery(FIND_BY_USERNAME_QUERY, Trainee.class);
            query.setParameter("username", username);

            Trainee trainee = query.getSingleResult();
            return Optional.of(trainee);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Training> getTrainingsByTraineeTrainingDTO(TraineeTrainingDTO traineeTrainingDTO) {
        TypedQuery<Training> query =
                getEntityManager().createQuery(GET_TRAININGS_BY_DATE_USERNAME_TRAINING_TYPE_QUERY, Training.class);
        query.setParameter("username", traineeTrainingDTO.getUserName());
        query.setParameter("trainingType", traineeTrainingDTO.getTrainingType());
        query.setParameter("startDate", traineeTrainingDTO.getStartDate());
        query.setParameter("endDate", traineeTrainingDTO.getEndDate());

        return query.getResultList();
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        Query query = getEntityManager().createQuery(DELETE_BY_USERNAME_QUERY);
        query.setParameter("username", username);
        query.executeUpdate();
    }
}
