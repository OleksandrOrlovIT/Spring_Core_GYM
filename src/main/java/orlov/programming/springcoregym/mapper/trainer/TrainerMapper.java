package orlov.programming.springcoregym.mapper.trainer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.dto.trainer.TrainerRegister;
import orlov.programming.springcoregym.dto.trainer.TrainerResponse;
import orlov.programming.springcoregym.dto.trainer.UpdateTrainerRequest;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.mapper.trainingtype.TrainingTypeMapper;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.service.training.TrainingTypeService;

import java.util.List;

@Component
@AllArgsConstructor
public class TrainerMapper {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    public Trainer trainerRegisterToTrainer(TrainerRegister trainerRegister){
        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerRegister.getFirstName());
        trainer.setLastName(trainerRegister.getLastName());
        trainer.setSpecialization(trainingTypeService.select(trainerRegister.getSpecializationId()));
        return trainer;
    }

    public UsernamePasswordUser trainerToUsernamePasswordUser(Trainer trainer){
        return new UsernamePasswordUser(trainer.getUsername(), trainer.getPassword());
    }

    public TrainerResponse trainerToTrainerResponse(Trainer trainer){
        TrainerResponse trainerResponse = new TrainerResponse();
        trainerResponse.setUsername(trainer.getUsername());
        trainerResponse.setFirstName(trainer.getFirstName());
        trainerResponse.setLastName(trainer.getLastName());
        trainerResponse.setSpecialization(trainingTypeMapper.trainingTypeToTrainingTypeResponse(trainer.getSpecialization()));
        return trainerResponse;
    }

    public List<TrainerResponse> trainersListToTrainerResponseList(List<Trainer> trainers){
        return trainers.stream()
                .map(this::trainerToTrainerResponse)
                .toList();
    }

    public Trainer updateTrainerRequestToTrainer(UpdateTrainerRequest request){
        Trainer trainer = new Trainer();
        trainer.setUsername(request.getUsername());
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setSpecialization(trainingTypeService.select(request.getSpecializationId()));
        trainer.setIsActive(request.getIsActive());
        return trainer;
    }
}
