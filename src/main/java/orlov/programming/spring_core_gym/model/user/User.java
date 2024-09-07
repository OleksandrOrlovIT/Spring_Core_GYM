package orlov.programming.spring_core_gym.model.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}