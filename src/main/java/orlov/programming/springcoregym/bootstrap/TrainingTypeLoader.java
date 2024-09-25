package orlov.programming.springcoregym.bootstrap;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.service.training.TrainingTypeService;

@Log4j2
@Component
@AllArgsConstructor
public class TrainingTypeLoader {

    private final TrainingTypeService trainingTypeService;

    public void loadDefaultTrainingTypes() {
        if (trainingTypeService.findAll().isEmpty()) {
            trainingTypeService.create(TrainingType.builder().trainingTypeName("Strength Training").build());
            trainingTypeService.create(TrainingType.builder().trainingTypeName("Cardio").build());
            trainingTypeService.create(TrainingType.builder().trainingTypeName("Flexibility").build());
            trainingTypeService.create(TrainingType.builder().trainingTypeName("Endurance").build());
            trainingTypeService.create(TrainingType.builder().trainingTypeName("Balance Training").build());
            log.info("Default TrainingTypes have been added.");
        }
    }
}
