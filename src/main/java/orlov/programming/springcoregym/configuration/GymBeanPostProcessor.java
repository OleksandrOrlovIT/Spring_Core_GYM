package orlov.programming.springcoregym.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.storage.Storage;

@Component
@Log4j2
public class GymBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof Storage) {
            log.info("Before Initialization: {}", beanName);
            ((Storage) bean).populateStorage();
            log.info(((Storage) bean).getStorage(Trainee.class).toString());
            log.info(((Storage) bean).getStorage(Trainer.class).toString());
            log.info(((Storage) bean).getStorage(Training.class).toString());
        }
        return bean;
    }
}
