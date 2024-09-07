package orlov.programming.spring_core_gym.model.training;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class TrainingType {
    private String trainingTypeName;
}
