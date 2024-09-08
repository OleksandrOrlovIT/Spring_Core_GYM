package orlov.programming.spring_core_gym.dao.impl.user;

import orlov.programming.spring_core_gym.dao.DAO;

public interface DAOUsernameFindable<T> extends DAO<T> {
    T findByUsername(String username);
}
