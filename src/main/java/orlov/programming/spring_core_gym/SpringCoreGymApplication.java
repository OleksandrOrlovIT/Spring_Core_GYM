package orlov.programming.spring_core_gym;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import orlov.programming.spring_core_gym.configuration.AppConfig;

public class SpringCoreGymApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
