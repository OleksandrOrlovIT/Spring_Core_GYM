package ua.orlov.springcoregym.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.orlov.springcoregym.model.ActionType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkload {

    private String username;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    private LocalDate trainingDate;

    private Long trainingDuration;

    private ActionType actionType;

}