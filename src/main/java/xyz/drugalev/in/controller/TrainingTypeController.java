package xyz.drugalev.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingTypeAlreadyExistsException;
import xyz.drugalev.exception.UserPrivilegeException;
import xyz.drugalev.service.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping(value = "/training-types")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    /**
     * Gets all training types.
     *
     * @return the list of training types
     */
    @Operation(summary = "Get all training types", description = "Gets all training types.")
    @GetMapping
    public List<TrainingTypeDto> getAll() {
        return trainingTypeService.getAll();
    }

    /**
     * Creates a training type.
     *
     * @param user              the user
     * @param trainingTypeDto the training type
     * @return the training type
     * @throws TrainingTypeAlreadyExistsException if the training type already exists
     * @throws UserPrivilegeException            if the user does not have the required privilege
     */
    @Operation(summary = "Create training type", description = "Creates a training type.")
    @PostMapping
    public TrainingTypeDto create(@RequestAttribute("user") User user, @RequestBody @Validated TrainingTypeDto trainingTypeDto) throws TrainingTypeAlreadyExistsException, UserPrivilegeException {
        return trainingTypeService.save(user, trainingTypeDto);
    }
}