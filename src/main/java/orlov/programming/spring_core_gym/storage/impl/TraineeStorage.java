package orlov.programming.spring_core_gym.storage.impl;

import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.storage.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class TraineeStorage implements Storage<Long, Trainee> {

    private static final HashMap<Long, Trainee> traineeHashMap;
    private static long nextId;
    private static final String filePath;

    static {
        traineeHashMap = new HashMap<>();
        nextId = 1L;
        filePath = "src/main/resources/traineeInitialize.txt";
    }

    @Override
    public Map<Long, Trainee> getStorage() {
        return traineeHashMap;
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

                traineeHashMap.put(nextId, constructTrainee(parts));
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

    private Trainee constructTrainee(String[] parts){
        return Trainee.builder()
                .firstName(parts[0])
                .lastName(parts[1])
                .username(parts[2])
                .password(parts[3])
                .isActive(Boolean.parseBoolean(parts[4]))
                .dateOfBirth(LocalDate.parse(parts[5]))
                .address(parts[6])
                .userId(nextId)
                .build();
    }
}