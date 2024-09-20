package orlov.programming.springcoregym.model.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
public class Trainer extends User {
    private String specialization;
    private Long userId;
}
