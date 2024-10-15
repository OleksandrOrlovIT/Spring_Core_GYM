package ua.orlov.springcoregym;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.orlov.springcoregym.configuration.AppConfig;

import java.util.Set;

public class SpringCoreGymApplication{

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistenceUnit");
        Set<EntityType<?>> entities = emf.getMetamodel().getEntities();
        System.out.println(entities);
    }
}
