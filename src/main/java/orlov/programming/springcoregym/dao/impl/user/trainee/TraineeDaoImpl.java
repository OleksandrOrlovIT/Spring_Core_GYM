package orlov.programming.springcoregym.dao.impl.user.trainee;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class TraineeDaoImpl extends AbstractDao<Trainee, Long> implements TraineeDao {

    @Override
    protected Class<Trainee> getEntityClass() {
        return Trainee.class;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        try {
            String jpql = "SELECT t FROM " + getEntityClass().getSimpleName() + " t WHERE t.username = :username";
            TypedQuery<Trainee> query = getEntityManager().createQuery(jpql, Trainee.class);
            query.setParameter("username", username);

            Trainee trainee = query.getSingleResult();
            return Optional.of(trainee);

        } catch (NoResultException e) {
            log.info("No trainee found with username: {}", username);
            return Optional.empty();
        }
    }

    @Override
    public List<Training> getTrainingsByDateUsernameTrainingType
            (LocalDate startDate, LocalDate endDate, String userName, String trainingType) {
        String jpql = "SELECT tr FROM Training tr "
                + "JOIN tr.trainee t "
                + "JOIN tr.trainingType tt "
                + "WHERE t.username = :username "
                + "AND tt.trainingTypeName = :trainingType "
                + "AND tr.trainingDate BETWEEN :startDate AND :endDate";

        TypedQuery<Training> query = getEntityManager().createQuery(jpql, Training.class);
        query.setParameter("username", userName);
        query.setParameter("trainingType", trainingType);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        String jpql = "DELETE FROM " + getEntityClass().getSimpleName() + " t WHERE t.username = :username";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("username", username);
        int rowsAffected = query.executeUpdate();

        log.info("Deleted {} trainee(s) with username: {}", rowsAffected, username);
    }
}
