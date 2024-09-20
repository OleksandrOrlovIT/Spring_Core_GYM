package orlov.programming.springcoregym.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for loading environment variables.
 */
@Configuration
public class EnvConfig {

    /**
     * Bean for loading environment variables using dotenv.
     *
     * @return Dotenv instance for accessing environment variables
     */
    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}
