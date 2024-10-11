package orlov.programming.springcoregym.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orlov.programming.springcoregym.dto.trainee.TraineeNamesResponse;
import orlov.programming.springcoregym.dto.trainingtype.TrainingTypeResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerFullResponse {

    private String firstName;

    private String lastName;

    private TrainingTypeResponse specialization;

    private Boolean isActive;

    private List<TraineeNamesResponse> trainees;
}
