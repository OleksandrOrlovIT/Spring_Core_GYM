package orlov.programming.springcoregym.model.training;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trainingTypeName;

    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainings;
}
