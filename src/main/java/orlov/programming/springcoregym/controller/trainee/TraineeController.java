package orlov.programming.springcoregym.controller.trainee;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import orlov.programming.springcoregym.dto.trainee.TraineeFullResponse;
import orlov.programming.springcoregym.dto.trainee.TraineeFullUsernameResponse;
import orlov.programming.springcoregym.dto.trainee.TraineeRegister;
import orlov.programming.springcoregym.dto.trainee.UpdateTraineeRequest;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.dto.user.UsernameUser;
import orlov.programming.springcoregym.facade.user.TraineeFacade;
import orlov.programming.springcoregym.mapper.trainee.TraineeMapper;
import orlov.programming.springcoregym.mapper.traineetrainer.TraineeTrainerMapper;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.Optional;

@RestController
@RequestMapping("/trainee")
@AllArgsConstructor
public class TraineeController {

    private final TraineeFacade traineeFacade;
    private final TraineeMapper traineeMapper;
    private final TraineeTrainerMapper traineeTrainerMapper;

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

    @GetMapping("/get-by-username")
    public ResponseEntity<TraineeFullResponse> getTraineeByUsername(@RequestBody @Validated UsernameUser usernameUser) {
        Optional<Trainee> traineeOptional = traineeFacade.selectTrainee(usernameUser.getUsername());

        return traineeOptional.map(trainee -> ResponseEntity.ok(traineeTrainerMapper.traineeToTraineeFullResponse(trainee)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping
    public ResponseEntity<TraineeFullUsernameResponse> updateTrainee(@RequestBody @Validated UpdateTraineeRequest request){
        Optional<Trainee> traineeOptional = traineeFacade.updateTrainee(traineeMapper.updateTraineeRequestToTrainee(request));

        return traineeOptional.map(trainee -> ResponseEntity.ok(traineeTrainerMapper.traineeToTraineeFullUsernameResponse(trainee)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTraineeByUsername(@RequestBody @Validated UsernameUser usernameUser){
        traineeFacade.deleteTrainee(usernameUser.getUsername());

        return ResponseEntity.noContent().build();
    }
}
