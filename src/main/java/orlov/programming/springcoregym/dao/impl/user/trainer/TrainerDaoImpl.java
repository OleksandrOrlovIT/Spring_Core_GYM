package orlov.programming.springcoregym.dao.impl.user.trainer;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import orlov.programming.springcoregym.dao.AbstractDao;
import orlov.programming.springcoregym.model.user.Trainer;

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
}
