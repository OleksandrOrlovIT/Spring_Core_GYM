package orlov.programming.springcoregym.mapper.trainingtype;

import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.dto.trainingtype.TrainingTypeResponse;
import orlov.programming.springcoregym.model.training.TrainingType;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingTypeMapper {

    public TrainingTypeResponse trainingTypeToTrainingTypeResponse(TrainingType trainingType) {
        return new TrainingTypeResponse(trainingType.getId(), trainingType.getTrainingTypeName());
    }

    public List<TrainingTypeResponse> trainingTypeListToTrainingTypeResponseList(List<TrainingType> trainingTypes) {
        return trainingTypes.stream()
                .map(this::trainingTypeToTrainingTypeResponse)
                .collect(Collectors.toList());
    }
}