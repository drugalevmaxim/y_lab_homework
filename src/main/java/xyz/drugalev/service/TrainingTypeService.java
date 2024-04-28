package xyz.drugalev.service;

import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.AccessDeniedException;

import java.sql.SQLException;
import java.util.List;

public interface TrainingTypeService {

    /**
     * Saves a new training type.
     *
     * @param user            the user who is saving the training type
     * @param trainingTypeDto the training type to save
     * @throws SQLException           if there is an error saving the training type to the database
     * @throws AccessDeniedException if the user does not have permission to save the training type
     */
    void save(User user, TrainingTypeDto trainingTypeDto) throws SQLException, AccessDeniedException;

    /**
     * Finds all training types.
     *
     * @return a list of all training types
     * @throws SQLException if there is an error retrieving the training types from the database
     */
    List<TrainingTypeDto> findAll() throws SQLException;
}