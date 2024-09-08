package orlov.programming.spring_core_gym.storage.impl;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import orlov.programming.spring_core_gym.model.user.Trainee;
import orlov.programming.spring_core_gym.storage.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class TraineeStorage implements Storage<Long, Trainee> {

    private final HashMap<Long, Trainee> traineeHashMap;
    private long nextId;
    @Setter
    private String filePath;

    public TraineeStorage() {
        traineeHashMap = new HashMap<>();
        nextId = 1L;
        setFilePath("src/main/resources/traineeInitialize.txt");
        log.info("Initializing TraineeStorage, traineeHashMap: {}, nextId = {}, filePath = {}",
                traineeHashMap, nextId, filePath);
    }

    @Override
    public Map<Long, Trainee> getStorage() {
        return traineeHashMap;
    }

    @Override
    public void populateStorage() {
        log.info("Populating TraineeStorage");
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
        } catch (Exception e){
            log.error(e);
        }
    }

    @Override
    public Long getLastKey() {
        return nextId;
    }

    protected Trainee constructTrainee(String[] parts){
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