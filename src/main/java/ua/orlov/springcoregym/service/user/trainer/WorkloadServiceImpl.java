package ua.orlov.springcoregym.service.user.trainer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;

@Log4j2
@Service
@AllArgsConstructor
public class WorkloadServiceImpl implements WorkloadService {

    private final RestTemplate restTemplate;

    private EurekaClient discoveryClient;

    @Override
    public String changeWorkload(TrainerWorkload trainerWorkload) {
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        String url = "";

        for (Application registeredApplication :  registry.getApplications().getRegisteredApplications()) {
            for (InstanceInfo instance : registeredApplication.getInstances()) {
                if ("GYM-TRAINER-WORKLOAD".equals(instance.getAppName())) {
                    String host = instance.getHostName();
                    String id = instance.getInstanceId();
                    int port = instance.getPort();
                    url = String.format("http://%s:%s:%d/api/v1/trainer/workload", id, host, port);
                    break;
                }
            }
        }

        if (url.isEmpty()) {
            throw new RuntimeException("No instance of GYM_TRAINER_WORKLOAD found in Eureka registry");
        }

        log.error("Calling URL: " + url);

        return restTemplate.postForObject(url, trainerWorkload, String.class);
    }
}
