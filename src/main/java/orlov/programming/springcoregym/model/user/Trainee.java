package orlov.programming.springcoregym.model.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import orlov.programming.springcoregym.model.training.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@Entity
public class Trainee extends User {

    private LocalDate dateOfBirth;

    private String address;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    @ManyToMany(mappedBy = "trainees")
    private List<Trainer> trainers;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "dateOfBirth = " + getDateOfBirth() + ", " +
                "address = " + getAddress() + ", " +
                "firstName = " + getFirstName() + ", " +
                "lastName = " + getLastName() + ", " +
                "username = " + getUsername() + ", " +
                "password = " + getPassword() + ", " +
                "isActive = " + getIsActive() + ")";
    }
}
