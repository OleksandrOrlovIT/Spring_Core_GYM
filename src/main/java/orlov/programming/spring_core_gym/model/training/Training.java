package orlov.programming.spring_core_gym.model.training;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class Training {
    private Trainee trainee;
    private Trainer trainer;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private LocalTime trainingDuration;
}
