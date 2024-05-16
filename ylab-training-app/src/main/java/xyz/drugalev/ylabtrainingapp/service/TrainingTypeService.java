package xyz.drugalev.ylabtrainingapp.service;

import xyz.drugalev.ylabtrainingapp.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.TrainingTypeAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;

import java.util.List;

/**
 * Training type service.
 *
 * @author Drugalev Maxim
 */
public interface TrainingTypeService {
    /**
     * Get all training types.
     *
     * @return list of training type DTOs
     */
    List<TrainingTypeDto> getAll();

    /**
     * Save training type.
     *
     * @param user          current user
     * @param trainingType training type DTO
     * @return saved training type DTO
     * @throws UserPrivilegeException           if user has no privilege to save training types
     * @throws TrainingTypeAlreadyExistsException if training type already exists
     */
    TrainingTypeDto save(User user, TrainingTypeDto trainingType) throws UserPrivilegeException, TrainingTypeAlreadyExistsException;
}