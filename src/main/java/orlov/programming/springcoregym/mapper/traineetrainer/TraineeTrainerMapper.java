package orlov.programming.springcoregym.mapper.traineetrainer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.dto.trainee.TraineeFullResponse;
import orlov.programming.springcoregym.dto.trainee.TraineeFullUsernameResponse;
import orlov.programming.springcoregym.dto.trainer.TrainerFullResponse;
import orlov.programming.springcoregym.dto.trainer.TrainerFullUsernameResponse;
import orlov.programming.springcoregym.mapper.trainee.TraineeMapper;
import orlov.programming.springcoregym.mapper.trainer.TrainerMapper;
import orlov.programming.springcoregym.mapper.trainingtype.TrainingTypeMapper;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;

@Component
@AllArgsConstructor
public class TraineeTrainerMapper {

    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;
    private final TrainingTypeMapper trainingTypeMapper;

    public TraineeFullResponse traineeToTraineeFullResponse(Trainee trainee){
        TraineeFullResponse response = new TraineeFullResponse();
        response.setFirstName(trainee.getFirstName());
        response.setLastName(trainee.getLastName());
        response.setDateOfBirth(trainee.getDateOfBirth());
        response.setAddress(trainee.getAddress());
        response.setActive(trainee.getIsActive());
        response.setTrainers(trainerMapper.trainersListToTrainerResponseList(trainee.getTrainers()));

        return response;
    }

    public TraineeFullUsernameResponse traineeToTraineeFullUsernameResponse(Trainee trainee){
        TraineeFullUsernameResponse response = new TraineeFullUsernameResponse();
        response.setUsername(trainee.getUsername());
        response.setFirstName(trainee.getFirstName());
        response.setLastName(trainee.getLastName());
        response.setDateOfBirth(trainee.getDateOfBirth());
        response.setAddress(trainee.getAddress());
        response.setActive(trainee.getIsActive());
        response.setTrainers(trainerMapper.trainersListToTrainerResponseList(trainee.getTrainers()));

        return response;
    }

    public TrainerFullResponse trainerToTrainerFullResponse(Trainer trainer){
        TrainerFullResponse response = new TrainerFullResponse();
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setSpecialization(trainingTypeMapper.trainingTypeToTrainingTypeResponse(trainer.getSpecialization()));
        response.setIsActive(trainer.getIsActive());
        response.setTrainees(traineeMapper.traineeListToTraineeNamesResponseList(trainer.getTrainees()));
        return response;
    }

    public TrainerFullUsernameResponse trainerToTrainerFullUsernameResponse(Trainer trainer){
        TrainerFullUsernameResponse response = new TrainerFullUsernameResponse();
        response.setUsername(trainer.getUsername());
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setSpecialization(trainingTypeMapper.trainingTypeToTrainingTypeResponse(trainer.getSpecialization()));
        response.setIsActive(trainer.getIsActive());
        response.setTrainees(traineeMapper.traineeListToTraineeNamesResponseList(trainer.getTrainees()));
        return response;
    }
}
