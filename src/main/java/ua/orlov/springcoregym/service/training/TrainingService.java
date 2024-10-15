package ua.orlov.springcoregym.service.training;

import ua.orlov.springcoregym.model.training.Training;

import java.util.List;

/**
 * Service interface for managing {@link Training} entities.
 */
public interface TrainingService {

    /**
     * Creates a new {@link Training} entity.
     *
     * @param training the training entity to create
     * @return the created {@link Training} entity
     * @throws java.lang.NullPointerException if the training or any of its required fields are null
     */
    Training create(Training training);

    /**
     * Retrieves a {@link Training} entity by its ID.
     *
     * @param id the ID of the training
     * @return the found {@link Training} entity
     * @throws java.util.NoSuchElementException if no training is found with the provided ID
     */
    Training select(Long id);

    /**
     * Retrieves all {@link Training} entities.
     *
     * @return a list of all {@link Training} entities
     */
    List<Training> getAll();
}
