package ua.orlov.springcoregym.service.user.trainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;
import ua.orlov.springcoregym.exception.BusinessLogicException;
import ua.orlov.springcoregym.model.HttpRequest;
import ua.orlov.springcoregym.service.http.CustomHttpSenderService;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class WorkloadServiceImpl implements WorkloadService {

    private static final String INSTANCE_NAME = "GYM-TRAINER-WORKLOAD";
    private static final String MICROSERVICE_UNAVAILABLE = INSTANCE_NAME + " microservice unavailable";
    private static final String URL_START = "https://";
    private static final String CHANGE_WORKLOAD_END_URL = "/api/v1/trainer/workload";

    private final DiscoveryClient discoveryClient;
    private final ObjectMapper objectMapper;
    private final CustomHttpSenderService httpSenderService;

    @Override
    @CircuitBreaker(name = "changeWorkloadCircuitBreaker", fallbackMethod = "logMicroserviceUnavailable")
    @Retry(name = "changeWorkloadRetry")
    public String changeWorkload(TrainerWorkload trainerWorkload) {
        List<ServiceInstance> instances = discoveryClient.getInstances(INSTANCE_NAME);
        if (instances.isEmpty()) {
            throw new RuntimeException("No instance of " + INSTANCE_NAME + " found in Eureka registry");
        }

        String url = constructUrlFromInstance(instances.get(0));
        String jsonPayload = "";

        try {
            jsonPayload = objectMapper.writeValueAsString(trainerWorkload);
        } catch (Exception e){
            log.error(e);
            throw new BusinessLogicException("Serialization error", e);
        }

        HttpRequest httpRequest = new HttpRequest(url, "POST");

        String result = httpSenderService.executeRequestWithEntity(httpRequest, jsonPayload);
        log.debug("Result of calling workload microservice = {}", result);
        return result;
    }

    private String extractIpAddress(ServiceInstance instance) {
        String instanceInfo = instance.toString();
        String foundStringForIpAddr = "ipAddr = ";
        int ipAddrIndex = instanceInfo.indexOf(foundStringForIpAddr) + foundStringForIpAddr.length();
        return instanceInfo.substring(ipAddrIndex, ipAddrIndex + 10);
    }

    private String constructUrlFromInstance(ServiceInstance instance) {
        String ipAddr = extractIpAddress(instance);
        int port = instance.getPort();

        return URL_START + ipAddr + ":" + port + CHANGE_WORKLOAD_END_URL;
    }

    public String logMicroserviceUnavailable(TrainerWorkload trainerWorkload, Throwable throwable) {
        String message = MICROSERVICE_UNAVAILABLE + " for workload: " + trainerWorkload;
        log.error(message, throwable);
        return message;
    }
}
