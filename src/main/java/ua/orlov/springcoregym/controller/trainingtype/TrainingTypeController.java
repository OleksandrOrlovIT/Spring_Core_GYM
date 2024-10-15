package ua.orlov.springcoregym.controller.trainingtype;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.orlov.springcoregym.dto.trainingtype.TrainingTypeResponse;
import ua.orlov.springcoregym.mapper.trainingtype.TrainingTypeMapper;
import ua.orlov.springcoregym.service.training.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping("/training-type")
@AllArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    @GetMapping
    public List<TrainingTypeResponse> getTrainingTypes() {
        return trainingTypeMapper.trainingTypeListToTrainingTypeResponseList(trainingTypeService.getAll());
    }
}
