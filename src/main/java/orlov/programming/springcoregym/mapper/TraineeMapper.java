package orlov.programming.springcoregym.mapper;

import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.dto.trainee.TraineeRegister;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.model.user.Trainee;

@Component
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
}
