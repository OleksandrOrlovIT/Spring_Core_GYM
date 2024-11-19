package ua.orlov.springcoregym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCoreGymApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCoreGymApplication.class, args);
    }
}
