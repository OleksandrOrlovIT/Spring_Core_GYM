package ua.orlov.springcoregym.controller.training;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.orlov.springcoregym.dto.training.CreateTrainingRequest;
import ua.orlov.springcoregym.dto.training.TraineeTrainingsRequest;
import ua.orlov.springcoregym.dto.training.TrainerTrainingRequest;
import ua.orlov.springcoregym.dto.training.TrainingFullResponse;
import ua.orlov.springcoregym.dto.trainingtype.TrainingTypeResponse;
import ua.orlov.springcoregym.mapper.training.TrainingMapper;
import ua.orlov.springcoregym.mapper.trainingtype.TrainingTypeMapper;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.service.training.TrainingService;
import ua.orlov.springcoregym.service.training.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping("/training")
@AllArgsConstructor
public class TrainingController {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    @GetMapping("/types")
    public List<TrainingTypeResponse> getTrainingTypes() {
        return trainingTypeMapper.trainingTypeListToTrainingTypeResponseList(trainingTypeService.getAll());
    }

    @GetMapping("/trainee")
    public List<TrainingFullResponse> getTrainingsByTraineeAndDate(@Validated @RequestBody TraineeTrainingsRequest request){
        List<Training> trainings = trainingService.getTrainingsByCriteria(request);

        return trainingMapper.trainingListToTrainingFullResponseList(trainings);
    }

    @GetMapping("/trainer")
    public List<TrainingFullResponse> getTrainingsByTrainerAndDate(@Validated @RequestBody TrainerTrainingRequest request){
        List<Training> trainings = trainingService.getTrainingsByCriteria(request);

        return trainingMapper.trainingListToTrainingFullResponseList(trainings);
    }

    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody @Validated CreateTrainingRequest request){
        Training training = trainingMapper.createTrainingRequestToTraining(request);

        trainingService.create(training);

        return ResponseEntity.ok().build();
    }
}
