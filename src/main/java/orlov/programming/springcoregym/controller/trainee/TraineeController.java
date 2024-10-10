package orlov.programming.springcoregym.controller.trainee;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import orlov.programming.springcoregym.dto.trainee.TraineeRegister;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.facade.user.TraineeFacade;
import orlov.programming.springcoregym.mapper.TraineeMapper;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.Optional;

@RestController
@RequestMapping("/trainee")
@AllArgsConstructor
public class TraineeController {

    private final TraineeFacade traineeFacade;
    private final TraineeMapper traineeMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<UsernamePasswordUser> registerTrainee(@Validated @RequestBody TraineeRegister traineeRegister) {
        Optional<Trainee> trainee = traineeFacade.createTrainee(traineeMapper.traineeRegisterToTrainee(traineeRegister));

        if (trainee.isPresent()) {
            UsernamePasswordUser user = traineeMapper.traineeToUsernamePasswordUser(trainee.get());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
