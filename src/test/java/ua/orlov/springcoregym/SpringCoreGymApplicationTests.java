package ua.orlov.springcoregym;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfig.class})
class SpringCoreGymApplicationTests {

    @Test
    void contextLoads() {
        SpringCoreGymApplication application = new SpringCoreGymApplication();
    }
}
