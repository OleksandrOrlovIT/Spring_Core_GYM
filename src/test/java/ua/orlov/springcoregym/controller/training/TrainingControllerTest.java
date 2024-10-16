package ua.orlov.springcoregym.controller.training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.orlov.springcoregym.dto.training.CreateTrainingRequest;
import ua.orlov.springcoregym.dto.training.TraineeTrainingsRequest;
import ua.orlov.springcoregym.dto.training.TrainerTrainingRequest;
import ua.orlov.springcoregym.mapper.training.TrainingMapper;
import ua.orlov.springcoregym.mapper.trainingtype.TrainingTypeMapper;
import ua.orlov.springcoregym.model.training.Training;
import ua.orlov.springcoregym.service.training.TrainingService;
import ua.orlov.springcoregym.service.training.TrainingTypeService;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingController trainingController;

    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setObjectMapper(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    void getTrainingTypesThenSuccess() throws Exception {
        when(trainingTypeService.getAll()).thenReturn(new ArrayList<>());
        when(trainingTypeMapper.trainingTypeListToTrainingTypeResponseList(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/training/types"))
                .andExpect(status().isOk())
                .andReturn();

        verify(trainingTypeService, times(1)).getAll();
        verify(trainingTypeMapper, times(1)).trainingTypeListToTrainingTypeResponseList(any());
    }

    @Test
    void getTrainingsByTraineeAndDateThenSuccess() throws Exception {
        TraineeTrainingsRequest request = new TraineeTrainingsRequest();
        request.setUsername("username");
        request.setStartDate(LocalDate.MIN);
        request.setEndDate(LocalDate.MAX);
        request.setTrainerUsername("trainerUsername");
        request.setTrainingTypeId(1L);

        when(trainingService.getTrainingsByCriteria(any(TraineeTrainingsRequest.class))).thenReturn(new ArrayList<>());
        when(trainingMapper.trainingListToTrainingFullResponseList(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/training/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        verify(trainingService, times(1)).getTrainingsByCriteria(any(TraineeTrainingsRequest.class));
        verify(trainingMapper, times(1)).trainingListToTrainingFullResponseList(any());
    }

    @Test
    void getTrainingsByTrainerAndDateThenSuccess() throws Exception {
        TrainerTrainingRequest request = new TrainerTrainingRequest();
        request.setUsername("username");
        request.setStartDate(LocalDate.MIN);
        request.setEndDate(LocalDate.MAX);
        request.setTraineeUsername("TraineeUsername");

        when(trainingService.getTrainingsByCriteria(any(TrainerTrainingRequest.class))).thenReturn(new ArrayList<>());
        when(trainingMapper.trainingListToTrainingFullResponseList(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/training/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        verify(trainingService, times(1)).getTrainingsByCriteria(any(TrainerTrainingRequest.class));
        verify(trainingMapper, times(1)).trainingListToTrainingFullResponseList(any());
    }

    @Test
    void createTrainingThenSuccess() throws Exception {
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTrainingName("trainingName");
        request.setTrainingDate(LocalDate.MIN);
        request.setTrainingTypeId(1L);
        request.setTrainerUsername("TrainerUsername");
        request.setTraineeUsername("TraineeUsername");
        request.setTrainingDuration(50L);

        when(trainingMapper.createTrainingRequestToTraining(any())).thenReturn(new Training());
        when(trainingService.create(any())).thenReturn(new Training());

        mockMvc.perform(post("/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        verify(trainingMapper, times(1)).createTrainingRequestToTraining(any());
        verify(trainingService, times(1)).create(any());
    }
}