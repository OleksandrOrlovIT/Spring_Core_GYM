package ua.orlov.springcoregym.service.user.trainer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;
import ua.orlov.springcoregym.model.HttpRequest;
import ua.orlov.springcoregym.service.http.CustomHttpSenderService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkloadServiceImplTest {

    private static final String INSTANCE_NAME = "GYM-TRAINER-WORKLOAD";
    private static final String MICROSERVICE_UNAVAILABLE = INSTANCE_NAME + " microservice unavailable";

    @Mock
    private DiscoveryClient discoveryClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CustomHttpSenderService httpSenderService;

    @InjectMocks
    private WorkloadServiceImpl workloadService;

    @Test
    void changeWorkloadThenException() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();

        when(discoveryClient.getInstances(any())).thenReturn(new ArrayList<>());

        RuntimeException e = assertThrows(RuntimeException.class, () -> workloadService.changeWorkload(trainerWorkload));

        assertEquals("No instance of " + INSTANCE_NAME + " found in Eureka registry", e.getMessage());

        verify(discoveryClient, times(1)).getInstances(any());
    }

    @Test
    void changeWorkloadThenSuccess() throws JsonProcessingException {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        ServiceInstance serviceInstance = new ServiceInstance() {
            @Override
            public String getServiceId() {
                return "";
            }

            @Override
            public String getHost() {
                return "";
            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public Map<String, String> getMetadata() {
                return Map.of();
            }
        };


        when(discoveryClient.getInstances(any())).thenReturn(List.of(serviceInstance));
        when(objectMapper.writeValueAsString(any())).thenReturn("val");
        when(httpSenderService.executeRequestWithEntity(any(HttpRequest.class), any())).thenReturn("result");

        String result = workloadService.changeWorkload(trainerWorkload);

        assertEquals("result", result);

        verify(discoveryClient, times(1)).getInstances(any());
        verify(objectMapper, times(1)).writeValueAsString(any());
        verify(httpSenderService, times(1)).executeRequestWithEntity(any(HttpRequest.class), any());
    }

    @Test
    void changeWorkloadThenExceptionWhileWritingValueAsString() throws JsonProcessingException {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        ServiceInstance serviceInstance = new ServiceInstance() {
            @Override
            public String getServiceId() {
                return "";
            }

            @Override
            public String getHost() {
                return "";
            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public Map<String, String> getMetadata() {
                return Map.of();
            }
        };


        when(discoveryClient.getInstances(any())).thenReturn(List.of(serviceInstance));
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException(""));

        RuntimeException e = assertThrows(RuntimeException.class, () ->  workloadService.changeWorkload(trainerWorkload));

        assertEquals("Serialization error", e.getMessage());

        verify(discoveryClient, times(1)).getInstances(any());
        verify(objectMapper, times(1)).writeValueAsString(any());
    }

    @Test
    void logMicroserviceUnavailableThenSuccess() {
        TrainerWorkload trainerWorkload = new TrainerWorkload();

        String message = MICROSERVICE_UNAVAILABLE + " for workload: " + trainerWorkload;

        assertEquals(message, workloadService.logMicroserviceUnavailable(trainerWorkload, new RuntimeException()));
    }
}
