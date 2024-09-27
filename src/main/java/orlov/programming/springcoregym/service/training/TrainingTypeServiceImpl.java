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

        Optional<TrainingType> trainingType = trainingTypeDao.findById(id);

        if (trainingType.isEmpty()) {
            throw new NoSuchElementException("TrainingType not found with id = " + id);
        }

        return trainingType.get();
    }

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeDao.findAll();
    }
}
