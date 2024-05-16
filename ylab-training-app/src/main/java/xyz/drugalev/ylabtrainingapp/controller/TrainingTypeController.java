package xyz.drugalev.ylabtrainingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.dto.TrainingTypeDto;
import xyz.drugalev.ylabtrainingapp.exception.TrainingTypeAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;
import xyz.drugalev.ylabtrainingapp.service.TrainingTypeService;

import java.util.List;

/**
 * Training type controller.
 *
 * @author Drugalev Maxim
 */
@RestController
@RequestMapping(value = "/training-types")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    /**
     * Gets all training types.
     *
     * @return a list of training type DTOs
     */
    @Operation(summary = "Get all training types", description = "Gets all training types.")
    @GetMapping
    public List<TrainingTypeDto> getTrainingTypes(@RequestAttribute("user") User user) {
        return trainingTypeService.getAll();
    }

    /**
     * Creates a training type.
     *
     * @param user            the current user
     * @param trainingTypeDto the training type DTO
     * @return a training type DTO
     * @throws TrainingTypeAlreadyExistsException if the training type already exists
     * @throws UserPrivilegeException             if the user does not have the required privilege
     */
    @Operation(summary = "Create training type", description = "Creates a training type.")
    @PostMapping
    public TrainingTypeDto create(@RequestAttribute("user") User user, @RequestBody @Validated TrainingTypeDto trainingTypeDto) throws TrainingTypeAlreadyExistsException, UserPrivilegeException {
        return trainingTypeService.save(user, trainingTypeDto);
    }
}