package ua.orlov.springcoregym.service.user.trainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;

@Log4j2
@Service
@AllArgsConstructor
public class WorkloadServiceImpl implements WorkloadService {

    private final CloseableHttpClient httpClient;

    private final DiscoveryClient discoveryClient;

    private final ObjectMapper objectMapper;

    @Override
    public String changeWorkload(TrainerWorkload trainerWorkload) {
        var instances = discoveryClient.getInstances("GYM-TRAINER-WORKLOAD");

        if (instances.isEmpty()) {
            throw new RuntimeException("No instance of GYM-TRAINER-WORKLOAD found in Eureka registry");
        }

        var instance = instances.get(0);
        String instanceInfo = instance.toString();
        String foundStringForIpAddr = "ipAddr = ";
        int ipAddrIndex = instanceInfo.indexOf(foundStringForIpAddr) + foundStringForIpAddr.length();
        String ipAddr = instanceInfo.substring(ipAddrIndex, ipAddrIndex + 10);

        String url = String.format("https://%s:%d/api/v1/trainer/workload", ipAddr, instance.getPort());
        log.info("Calling URL: " + url);

        try {
            String json = objectMapper.writeValueAsString(trainerWorkload);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

            HttpPost post = new HttpPost(url);
            post.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            log.error(e);
            return "Exception" + e.getMessage();
        }
    }
}
