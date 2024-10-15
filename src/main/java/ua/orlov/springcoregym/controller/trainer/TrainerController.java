package ua.orlov.springcoregym.controller.trainer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.orlov.springcoregym.dto.trainer.*;
import ua.orlov.springcoregym.dto.trainer.*;
import ua.orlov.springcoregym.dto.user.UsernameIsActiveUser;
import ua.orlov.springcoregym.dto.user.UsernamePasswordUser;
import ua.orlov.springcoregym.dto.user.UsernameUser;
import ua.orlov.springcoregym.mapper.traineetrainer.TraineeTrainerMapper;
import ua.orlov.springcoregym.mapper.trainer.TrainerMapper;
import ua.orlov.springcoregym.model.user.Trainee;
import ua.orlov.springcoregym.model.user.Trainer;
import ua.orlov.springcoregym.service.user.trainer.TrainerService;
import ua.orlov.springcoregym.util.model.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainer")
@AllArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TraineeTrainerMapper traineeTrainerMapper;

    @PostMapping
    public ResponseEntity<UsernamePasswordUser> registerTrainer(@Validated @RequestBody TrainerRegister trainerRegister) {
        Trainer trainer = trainerService.create(trainerMapper.trainerRegisterToTrainer(trainerRegister));

        UsernamePasswordUser user = trainerMapper.trainerToUsernamePasswordUser(trainer);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username")
    public ResponseEntity<TrainerFullResponse> getTrainerByUsername(@RequestBody @Validated UsernameUser usernameUser) {
        Trainer trainer = trainerService.getByUserNameWithTrainees(usernameUser.getUsername());

        return ResponseEntity.ok(traineeTrainerMapper.trainerToTrainerFullResponse(trainer));
    }

    @PutMapping
    public ResponseEntity<TrainerFullUsernameResponse> updateTrainer(@RequestBody @Validated UpdateTrainerRequest request) {
        Trainer trainer = trainerService.update(trainerMapper.updateTrainerRequestToTrainer(request));

        return ResponseEntity.ok(traineeTrainerMapper.trainerToTrainerFullUsernameResponse(trainer));
    }

    @GetMapping("/without-trainee")
    public List<TrainerResponse> getTrainersWithoutTrainee(@RequestBody @Validated UsernameUser usernameUser) {
        List<Trainer> foundTrainers = trainerService
                .getTrainersWithoutPassedTrainee(usernameUser.getUsername(), new Pageable(0, 10));

        return trainerMapper.trainersListToTrainerResponseList(foundTrainers);
    }

    @PatchMapping("/active")
    public ResponseEntity<?> activateDeactivateTrainee(@RequestBody @Validated UsernameIsActiveUser request){
        trainerService.activateDeactivateTrainer(request.getUsername(), request.getIsActive());

        return ResponseEntity.ok().build();
    }
}
