package orlov.programming.spring_core_gym.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.model.training.Training;
import orlov.programming.spring_core_gym.model.training.TrainingType;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.storage.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingStorage implements Storage<Long, Training> {

    private static final HashMap<Long, Training> trainingHashMap;
    private static Long nextId;
    private final Map<Long, Trainee> traineeHashMap;
    private final Map<Long, Trainer> trainerHashMap;

    private static final String filePath;

    @Autowired
    public TrainingStorage(TraineeStorage traineeStorage, TrainerStorage trainerStorage) {
        traineeHashMap = traineeStorage.getStorage();
        trainerHashMap = trainerStorage.getStorage();
    }

    static {
        trainingHashMap = new HashMap<>();
        nextId = 1L;
        filePath = "src/main/resources/trainingInitialize.txt";
    }

    @Override
    public Map<Long, Training> getStorage() {
        return trainingHashMap;
    }

    @Override
    public void populateStorage() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                for(int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                trainingHashMap.put(nextId, constructTraining(parts));
                nextId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getLastKey() {
        return nextId;
    }

    private Training constructTraining(String[] parts){
        return Training.builder()
                .trainee(traineeHashMap.get(Long.parseLong(parts[0])))
                .trainer(trainerHashMap.get(Long.parseLong(parts[1])))
                .trainingName(parts[2])
                .trainingType(TrainingType.valueOf(parts[3]))
                .trainingDate(LocalDate.parse(parts[4]))
                .trainingDuration(LocalTime.parse(parts[5]))
                .build();
    }
}