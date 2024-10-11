package orlov.programming.springcoregym.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeNamesResponse {

    private String username;

    private String firstName;

    private String lastName;
}
