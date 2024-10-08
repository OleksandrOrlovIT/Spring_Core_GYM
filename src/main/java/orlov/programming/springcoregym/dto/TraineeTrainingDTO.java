package orlov.programming.springcoregym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class TraineeTrainingDTO {
    LocalDate startDate;
    LocalDate endDate;
    String userName;
    String trainingType;
}
