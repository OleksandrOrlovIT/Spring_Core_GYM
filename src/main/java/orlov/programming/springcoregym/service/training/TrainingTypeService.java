package orlov.programming.springcoregym.service.training;

import orlov.programming.springcoregym.model.training.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    TrainingType create(TrainingType trainingType);

    TrainingType select(Long id);

    List<TrainingType> getAll();
}
