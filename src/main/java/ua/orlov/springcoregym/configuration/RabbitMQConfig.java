package ua.orlov.springcoregym.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final String RABBITMQ_HOST;
    private final Integer RABBITMQ_PORT;
    private final String RABBITMQ_USERNAME;
    private final String RABBITMQ_PASSWORD;
    private final String RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME;

    public RabbitMQConfig(@Value("${RABBITMQ.HOST}") String RABBITMQ_HOST,
                          @Value("${RABBITMQ.PORT}")Integer RABBITMQ_PORT,
                          @Value("${RABBITMQ.USERNAME}")String RABBITMQ_USERNAME,
                          @Value("${RABBITMQ.PASSWORD}")String RABBITMQ_PASSWORD,
                          @Value("${RABBITMQ.TRAINER-WORKLOAD-QUEUE-NAME}")String RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME) {
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        this.RABBITMQ_PORT = RABBITMQ_PORT;
        this.RABBITMQ_USERNAME = RABBITMQ_USERNAME;
        this.RABBITMQ_PASSWORD = RABBITMQ_PASSWORD;
        this.RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME = RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST, RABBITMQ_PORT);
        connectionFactory.setUsername(RABBITMQ_USERNAME);
        connectionFactory.setPassword(RABBITMQ_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue trainerWorkloadQueue() {
        return new Queue(RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME, false);
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
}
