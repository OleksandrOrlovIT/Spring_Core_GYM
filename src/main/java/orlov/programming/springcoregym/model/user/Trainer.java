package orlov.programming.springcoregym.model.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;

import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
public class Trainer extends User {

    @ManyToOne(optional = false)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType specialization;

    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings;

    @ManyToMany
    @JoinTable(
            name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private List<Trainee> trainees;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "specialization = " + getSpecialization() + ", " +
                "firstName = " + getFirstName() + ", " +
                "lastName = " + getLastName() + ", " +
                "username = " + getUsername() + ", " +
                "password = " + getPassword() + ", " +
                "isActive = " + getIsActive() + ")";
    }
}
