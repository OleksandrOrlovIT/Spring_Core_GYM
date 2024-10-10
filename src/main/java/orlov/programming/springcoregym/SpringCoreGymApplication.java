package orlov.programming.springcoregym;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import orlov.programming.springcoregym.configuration.AppConfig;

public class SpringCoreGymApplication{

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
