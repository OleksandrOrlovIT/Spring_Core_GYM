package ua.orlov.springcoregym.service.messages.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;
import ua.orlov.springcoregym.service.messages.MessageSender;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class RabbitMQMessageSender implements MessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME;

    public RabbitMQMessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,
                                 @Value("${RABBITMQ.TRAINER-WORKLOAD-QUEUE-NAME}") String RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME = RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME;
    }

    @Override
    public void sendMessage(String message, String json, String queueName){
        try {
            Map<String, String> messageContent = new HashMap<>();
            messageContent.put("subject", message);
            messageContent.put("content", json);

            String jsonMessage = objectMapper.writeValueAsString(messageContent);
            rabbitTemplate.convertAndSend(queueName, jsonMessage);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void sendMessageToTrainerWorkload(TrainerWorkload trainerWorkload) {
        try {
            String json = objectMapper.writeValueAsString(trainerWorkload);
            sendMessage("Trainer workload", json, RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
