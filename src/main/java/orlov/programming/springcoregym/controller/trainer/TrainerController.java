package orlov.programming.springcoregym.controller.trainer;

import lombok.AllArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import orlov.programming.springcoregym.dto.trainer.*;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.dto.user.UsernameUser;
import orlov.programming.springcoregym.facade.user.TrainerFacade;
import orlov.programming.springcoregym.mapper.traineetrainer.TraineeTrainerMapper;
import orlov.programming.springcoregym.mapper.trainer.TrainerMapper;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.util.model.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainer")
@AllArgsConstructor
public class TrainerController {
    private final TrainerFacade trainerFacade;
    private final TrainerMapper trainerMapper;
    private final TraineeTrainerMapper traineeTrainerMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<UsernamePasswordUser> registerTrainer(@Validated @RequestBody TrainerRegister trainerRegister) {
        Optional<Trainer> trainer = trainerFacade.createTrainer(trainerMapper.trainerRegisterToTrainer(trainerRegister));

        if (trainer.isPresent()) {
            UsernamePasswordUser user = trainerMapper.trainerToUsernamePasswordUser(trainer.get());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/get-by-username")
    public ResponseEntity<TrainerFullResponse> getTrainerByUsername(@RequestBody @Validated UsernameUser usernameUser) {
        Optional<Trainer> trainerOptional = trainerFacade.selectTrainer(usernameUser.getUsername());

        return trainerOptional
                .map(trainee -> ResponseEntity.ok(traineeTrainerMapper.trainerToTrainerFullResponse(trainee)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping
    public ResponseEntity<TrainerFullUsernameResponse> updateTrainer(@RequestBody @Validated UpdateTrainerRequest request){
        Optional<Trainer> traineeOptional =
                trainerFacade.updateTrainer(trainerMapper.updateTrainerRequestToTrainer(request));

        return traineeOptional
                .map(trainer -> ResponseEntity.ok(traineeTrainerMapper.trainerToTrainerFullUsernameResponse(trainer)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @GetMapping("/without-trainee")
    public List<TrainerResponse> getTrainersWithoutTrainees(@RequestBody @Validated UsernameUser usernameUser){
        List<Trainer> foundTrainers = trainerFacade
                .getTrainersWithoutPassedTrainee(usernameUser.getUsername(), new Pageable(0, 10));

        return trainerMapper.trainersListToTrainerResponseList(foundTrainers);
    }
}
