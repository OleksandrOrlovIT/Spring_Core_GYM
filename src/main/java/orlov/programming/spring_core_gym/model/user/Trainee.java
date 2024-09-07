package orlov.programming.spring_core_gym.model.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Setter
@Getter
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private Long userId;
}