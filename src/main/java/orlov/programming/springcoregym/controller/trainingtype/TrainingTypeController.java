package orlov.programming.springcoregym.controller.trainingtype;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import orlov.programming.springcoregym.dto.trainingtype.TrainingTypeResponse;
import orlov.programming.springcoregym.mapper.trainingtype.TrainingTypeMapper;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.service.training.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping("/training-type")
@AllArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;
    private final AuthenticationService authenticationService;

    @GetMapping
    public List<TrainingTypeResponse> getTrainingTypes() {
        authenticationService.isUserLogged();
        return trainingTypeMapper.trainingTypeListToTrainingTypeResponseList(trainingTypeService.getAll());
    }
}
