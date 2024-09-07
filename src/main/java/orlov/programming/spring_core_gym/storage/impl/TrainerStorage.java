package orlov.programming.spring_core_gym.storage.impl;

import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.model.user.Trainer;
import orlov.programming.spring_core_gym.storage.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainerStorage implements Storage<Long, Trainer> {

    private static final HashMap<Long, Trainer> trainerHashMap;
    private static Long nextId;
    private static final String filePath;

    static {
        trainerHashMap = new HashMap<>();
        nextId = 1L;
        filePath = "src/main/resources/trainerInitialize.txt";
    }

    @Override
    public Map<Long, Trainer> getStorage() {
        return trainerHashMap;
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

                trainerHashMap.put(nextId, constructTrainer(parts));
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

    private Trainer constructTrainer(String[] parts){
        return Trainer.builder()
                .firstName(parts[0])
                .lastName(parts[1])
                .username(parts[2])
                .password(parts[3])
                .isActive(Boolean.parseBoolean(parts[4]))
                .specialization(parts[5])
                .userId(nextId)
                .build();
    }
}