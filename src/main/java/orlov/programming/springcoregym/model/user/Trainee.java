package orlov.programming.springcoregym.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import orlov.programming.springcoregym.model.training.Training;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Setter
@Getter
@Entity
public class Trainee extends User {

    private LocalDate dateOfBirth;

    private String address;

    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings;

    @ManyToMany(mappedBy = "trainees")
    private List<Trainer> trainers;
}
