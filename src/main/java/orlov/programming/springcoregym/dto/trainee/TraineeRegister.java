package orlov.programming.springcoregym.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TraineeRegister {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    private LocalDate dateOfBirth;

    private String address;
}
