package ua.orlov.springcoregym.controller.integration.training;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.orlov.springcoregym.controller.integration.config.LoginComponent;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/sql/training/populate_trainings_encrypted.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/prune_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class TrainingControllerIntegrationTest {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginComponent loginComponent;

    @BeforeEach
    void setUp() {
        loginComponent = new LoginComponent(httpClient, objectMapper);
    }

    @Test
    void getTrainingTypesThenAccessDenied() throws IOException {
        HttpGet get = new HttpGet("https://localhost:8443/api/v1/training/types");

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            assertEquals(403, response.getStatusLine().getStatusCode());
        }
    }

    @Test
    void getTrainingTypesThenSuccess() throws Exception {
        String token = loginComponent.loginAsUser("testtrainer", "password");

        HttpGet get = new HttpGet("https://localhost:8443/api/v1/training/types");
        get.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            assertEquals(200, response.getStatusLine().getStatusCode());
            assertEquals("[{\"id\":1,\"trainingTypeName\":\"testTrainingType1\"}]", EntityUtils.toString(response.getEntity()));
        }
    }
}
