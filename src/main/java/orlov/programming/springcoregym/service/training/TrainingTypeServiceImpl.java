package orlov.programming.springcoregym.service.training;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import orlov.programming.springcoregym.dao.impl.training.TrainingTypeDao;
import orlov.programming.springcoregym.model.training.TrainingType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public TrainingType create(TrainingType trainingType) {
        Objects.requireNonNull(trainingType, "TrainingType must not be null");
        Objects.requireNonNull(trainingType.getTrainingTypeName(), "TrainingType name must not be null");

        return trainingTypeDao.create(trainingType);
    }

    @Override
    public TrainingType select(Long id) {
        Objects.requireNonNull(id, "TrainingType id must not be null");

        return trainingTypeDao.getById(id)
                .orElseThrow(() -> new NoSuchElementException("TrainingType not found with id = " + id));
    }

    @Override
    public List<TrainingType> getAll() {
        return trainingTypeDao.getAll();
    }
}
