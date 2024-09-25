package orlov.programming.springcoregym.dao.impl.user.trainer;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class TrainerDaoImpl extends AbstractDao<Trainer, Long> implements TrainerDao {

    @Override
    protected Class<Trainer> getEntityClass() {
        return Trainer.class;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        try {
            String jpql = "SELECT t FROM " + getEntityClass().getSimpleName() + " t WHERE t.username = :username";
            TypedQuery<Trainer> query = getEntityManager().createQuery(jpql, Trainer.class);
            query.setParameter("username", username);

            Trainer trainer = query.getSingleResult();
            return Optional.of(trainer);

        } catch (NoResultException e) {
            log.info("No trainer found with username: {}", username);
            return Optional.empty();
        }
    }

    @Override
    public List<Training> getTrainingsByDateAndUsername(LocalDate startDate, LocalDate endDate, String userName) {
        String jpql = "SELECT tr FROM Training tr "
                + "JOIN tr.trainer t "
                + "WHERE t.username = :username "
                + "AND tr.trainingDate BETWEEN :startDate AND :endDate";

        TypedQuery<Training> query = getEntityManager().createQuery(jpql, Training.class);
        query.setParameter("username", userName);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    @Override
    public List<Trainer> getTrainersWithoutPassedTrainee(Trainee trainee) {
        String jpql = "SELECT tr FROM Trainer tr " +
                "WHERE :trainee NOT MEMBER OF tr.trainees";
        TypedQuery<Trainer> query = getEntityManager().createQuery(jpql, Trainer.class);
        query.setParameter("trainee", trainee);
        return query.getResultList();
    }

    @Override
    public List<Trainer> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        String jpql = "SELECT t FROM " + getEntityClass().getSimpleName() + " t WHERE t.id IN :ids";
        TypedQuery<Trainer> query = getEntityManager().createQuery(jpql, Trainer.class);
        query.setParameter("ids", ids);

        return query.getResultList();
    }
}
