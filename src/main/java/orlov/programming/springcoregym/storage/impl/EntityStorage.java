package orlov.programming.springcoregym.storage.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.model.training.Training;
import orlov.programming.springcoregym.model.training.TrainingType;
import orlov.programming.springcoregym.model.user.Trainee;
import orlov.programming.springcoregym.model.user.Trainer;
import orlov.programming.springcoregym.storage.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class EntityStorage implements Storage {

    private final Map<Class<?>, Map<Long, ?>> storageMap = new HashMap<>();
    private final Map<Class<?>, Long> idCounters = new HashMap<>();

    private static final String TRAINEE_FILEPATH = "src/main/resources/traineeInitialize.txt";
    private static final String TRAINER_FILEPATH = "src/main/resources/trainerInitialize.txt";
    private static final String TRAINING_FILEPATH = "src/main/resources/trainingInitialize.txt";

    public EntityStorage() {
        storageMap.put(Trainee.class, new HashMap<Long, Trainee>());
        storageMap.put(Trainer.class, new HashMap<Long, Trainer>());
        storageMap.put(Training.class, new HashMap<Long, Training>());

        idCounters.put(Trainee.class, 1L);
        idCounters.put(Trainer.class, 1L);
        idCounters.put(Training.class, 1L);
    }

    @SuppressWarnings("unchecked")
    public <E> Map<Long, E> getStorage(Class<E> entityClass) {
        return (Map<Long, E>) storageMap.get(entityClass);
    }

    public <E> Long getNextId(Class<E> entityClass) {
        Long nextId = idCounters.get(entityClass);
        idCounters.put(entityClass, nextId + 1);
        return nextId;
    }

    public void populateStorage() {
        log.info("Populating storage for all entities");
        populateTrainees();
        populateTrainers();
        populateTrainings();
    }

    private void populateTrainees() {
        log.info("Populating trainees");
        populateEntities(Trainee.class, TRAINEE_FILEPATH, this::constructTrainee);
    }

    private void populateTrainers() {
        log.info("Populating trainers");
        populateEntities(Trainer.class, TRAINER_FILEPATH, this::constructTrainer);
    }

    private void populateTrainings() {
        log.info("Populating trainings");
        HashMap<Long, Trainee> trainees = (HashMap<Long, Trainee>) getStorage(Trainee.class);
        HashMap<Long, Trainer> trainers = (HashMap<Long, Trainer>) getStorage(Trainer.class);

        populateEntities(Training.class, TRAINING_FILEPATH, (parts, id) ->
                constructTraining(parts, trainees.get(Long.parseLong(parts[0])), trainers.get(Long.parseLong(parts[1]))));
    }

    protected <E> void populateEntities(Class<E> entityClass, String filePath, EntityConstructor<E> constructor) {
        log.info("Populating entities for {}", entityClass.getSimpleName());
        HashMap<Long, E> entityMap = (HashMap<Long, E>) getStorage(entityClass);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                Long id = getNextId(entityClass);
                entityMap.put(id, constructor.construct(parts, id));
            }
        } catch (Exception e) {
            log.error("Error populating entities for {}: {}", entityClass.getSimpleName(), e.getMessage());
        }
    }

    protected Trainee constructTrainee(String[] parts, Long id){
        return Trainee.builder()
                .firstName(parts[0])
                .lastName(parts[1])
                .username(parts[2])
                .password(parts[3])
                .isActive(Boolean.parseBoolean(parts[4]))
                .dateOfBirth(LocalDate.parse(parts[5]))
                .address(parts[6])
                .userId(id)
                .build();
    }

    protected Trainer constructTrainer(String[] parts, Long id){
        return Trainer.builder()
                .firstName(parts[0])
                .lastName(parts[1])
                .username(parts[2])
                .password(parts[3])
                .isActive(Boolean.parseBoolean(parts[4]))
                .specialization(parts[5])
                .userId(id)
                .build();
    }

    protected Training constructTraining(String[] parts, Trainee trainee, Trainer trainer){
        return Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(parts[2])
                .trainingType(TrainingType.valueOf(parts[3]))
                .trainingDate(LocalDate.parse(parts[4]))
                .trainingDuration(LocalTime.parse(parts[5]))
                .build();
    }

    @FunctionalInterface
    protected interface EntityConstructor<E> {
        E construct(String[] parts, Long id);
    }
}
