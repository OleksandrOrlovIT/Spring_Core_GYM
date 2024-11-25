package ua.orlov.springcoregym.service.messages;

import ua.orlov.springcoregym.dto.trainer.TrainerWorkload;

public interface MessageSender {

    void sendMessage(String message, String json, String queueName);

    void sendMessageToTrainerWorkload(TrainerWorkload trainerWorkload);
}
