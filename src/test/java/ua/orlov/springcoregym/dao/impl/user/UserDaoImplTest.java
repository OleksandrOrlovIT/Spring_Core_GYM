package ua.orlov.springcoregym.dao.impl.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.orlov.springcoregym.dao.impl.TestDaoConfig;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Transactional
@Sql(scripts = "/sql/user/populate_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @Test
    void isUserNameMatchPassword_thenSuccess(){
        String userName = "testUser";
        String password = "password";

        assertTrue(userDao.isUserNameMatchPassword(userName, password));
    }

    @Test
    void changeUserPassword_thenSuccess(){
        String userName = "testUser";
        String oldPassword = "password";
        String newPassword = "newPassword";

        assertTrue(userDao.changeUserPassword(userName, oldPassword, newPassword));
    }
}
