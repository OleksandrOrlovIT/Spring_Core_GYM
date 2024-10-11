package orlov.programming.springcoregym.mapper.trainee;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.dto.trainee.*;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.model.user.Trainee;

import java.util.List;

@Component
@AllArgsConstructor
public class TraineeMapper {

    public Trainee traineeRegisterToTrainee(TraineeRegister traineeRegister){
        return Trainee.builder()
                .firstName(traineeRegister.getFirstName())
                .lastName(traineeRegister.getLastName())
                .dateOfBirth(traineeRegister.getDateOfBirth())
                .address(traineeRegister.getAddress())
                .build();
    }

    public UsernamePasswordUser traineeToUsernamePasswordUser(Trainee trainee){
        return new UsernamePasswordUser(trainee.getUsername(), trainee.getPassword());
    }

    public Trainee updateTraineeRequestToTrainee(UpdateTraineeRequest request){
        Trainee trainee = new Trainee();
        trainee.setUsername(request.getUsername());
        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());
        trainee.setIsActive(request.getIsActive());
        return trainee;
    }

    public TraineeNamesResponse traineeToTraineeNamesResponse(Trainee trainee){
        TraineeNamesResponse response = new TraineeNamesResponse();
        response.setUsername(trainee.getUsername());
        response.setFirstName(trainee.getFirstName());
        response.setLastName(trainee.getLastName());
        return response;
    }

    public List<TraineeNamesResponse> traineeListToTraineeNamesResponseList(List<Trainee> trainees){
        return trainees.stream()
                .map(this::traineeToTraineeNamesResponse)
                .toList();
    }
}
