package orlov.programming.spring_core_gym.configuration;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.storage.Storage;
import orlov.programming.spring_core_gym.storage.impl.TraineeStorage;
import orlov.programming.spring_core_gym.storage.impl.TrainerStorage;
import orlov.programming.spring_core_gym.storage.impl.TrainingStorage;

@Component
public class GymBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof TraineeStorage || bean instanceof TrainerStorage || bean instanceof TrainingStorage) {
            System.out.println("Before Initialization: " + beanName);
            ((Storage<?, ?>) bean).populateStorage();
            System.out.println(((Storage<?, ?>) bean).getStorage().toString());
        }
        return bean;
    }
}