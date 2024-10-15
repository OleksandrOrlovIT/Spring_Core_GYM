package ua.orlov.springcoregym.controller.trainee;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.orlov.springcoregym.dto.trainee.*;
import ua.orlov.springcoregym.dto.user.UsernamePasswordUser;
import ua.orlov.springcoregym.dto.user.UsernameUser;
import ua.orlov.springcoregym.mapper.trainee.TraineeMapper;
import ua.orlov.springcoregym.mapper.traineetrainer.TraineeTrainerMapper;
import ua.orlov.springcoregym.mapper.user.UserMapper;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.service.user.trainee.TraineeService;

import java.util.List;

@RestController
@RequestMapping("/trainee")
@AllArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TraineeTrainerMapper traineeTrainerMapper;
    private final UserMapper userMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<UsernamePasswordUser> registerTrainee(@Validated @RequestBody TraineeRegister traineeRegister) {
        Trainee trainee = traineeService.create(traineeMapper.traineeRegisterToTrainee(traineeRegister));
        UsernamePasswordUser user = traineeMapper.traineeToUsernamePasswordUser(trainee);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-by-username")
    public ResponseEntity<TraineeFullResponse> getTraineeByUsername(@RequestBody @Validated UsernameUser usernameUser) {
        Trainee trainee = traineeService.getByUserNameWithTrainers(usernameUser.getUsername());

        return ResponseEntity.ok(traineeTrainerMapper.traineeToTraineeFullResponse(trainee));
    }

    @PutMapping
    public ResponseEntity<TraineeFullUsernameResponse> updateTrainee(@RequestBody @Validated UpdateTraineeRequest request) {
        Trainee trainee = traineeService.update(traineeMapper.updateTraineeRequestToTrainee(request));

        return ResponseEntity.ok(traineeTrainerMapper.traineeToTraineeFullUsernameResponse(trainee));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTraineeByUsername(@RequestBody @Validated UsernameUser usernameUser) {
        traineeService.deleteByUsername(usernameUser.getUsername());

        return ResponseEntity.noContent().build();
    }
        //changereturn value
    @PutMapping("/trainers")
    public List<Trainer> updateTraineeTrainersList(@RequestBody @Validated UpdateTraineeTrainersListRequest request) {
        return traineeService.updateTraineeTrainers(request.getUsername(),
                userMapper.mapUsernameUserListToStringList(request.getTrainers()));
    }
}
