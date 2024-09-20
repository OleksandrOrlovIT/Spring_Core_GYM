package orlov.programming.springcoregym.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import orlov.programming.springcoregym.model.training.Training;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
@Entity
public class Trainer extends User {

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings;
}
