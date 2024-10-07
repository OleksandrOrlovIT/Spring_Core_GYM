package orlov.programming.springcoregym.service.training;

import orlov.programming.springcoregym.model.training.Training;

import java.util.List;

public interface TrainingService {
    Training create(Training training);

    Training select(Long id);

    List<Training> getAll();
}
