package orlov.programming.springcoregym.facade.training.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.facade.training.TrainingFacade;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;
import orlov.programming.springcoregym.service.training.TrainingService;

@AllArgsConstructor
@Component
public class TrainingFacadeImpl implements TrainingFacade {

    private final AuthenticationService authenticationService;
    private final TrainingService trainingService;

    @Override
    public Training addTraining(Training training) {
        authenticationService.isUserLogged();
        return trainingService.create(training);
    }
}
