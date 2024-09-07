package orlov.programming.spring_core_gym.model.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Trainer extends User{
    private String specialization;
    private Long UserId;
}
