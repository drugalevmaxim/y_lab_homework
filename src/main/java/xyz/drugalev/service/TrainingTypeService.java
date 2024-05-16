package xyz.drugalev.service;

import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingTypeAlreadyExistsException;
import xyz.drugalev.exception.UserPrivilegeException;

import java.util.List;

/**
 * Interface for working with training types.
 */
public interface TrainingTypeService {
    /**
     * Finds all training types.
     *
     * @return a list of all training types
     */
    List<TrainingTypeDto> getAll();

    /**
     * Saves a new training type.
     *
     * @param user         the user who is saving the training type
     * @param trainingType the training type to save
     * @throws UserPrivilegeException             if the user does not have permission to save the training type
     * @throws TrainingTypeAlreadyExistsException if training type already exists
     */
    TrainingTypeDto save(User user, TrainingTypeDto trainingType) throws UserPrivilegeException, TrainingTypeAlreadyExistsException;
}
