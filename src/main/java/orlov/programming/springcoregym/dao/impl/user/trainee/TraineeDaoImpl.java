package orlov.programming.springcoregym.dao.impl.user.trainee;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.model.user.Trainee;

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
}
