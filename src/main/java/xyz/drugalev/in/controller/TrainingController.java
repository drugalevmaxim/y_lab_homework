package xyz.drugalev.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingDoesNotExistsException;
import xyz.drugalev.service.TrainingService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TrainingController {
    private final TrainingService trainingService;

    /**
     * Gets the user's trainings.
     *
     * @param user  the user
     * @param start the start date
     * @param end   the end date
     * @return the list of trainings
     * @throws DateTimeParseException if the start or end date is not in the correct format
     */
    @Operation(summary = "Get user's trainings", description = "Gets the user's trainings.")
    @GetMapping
    public List<TrainingDto> getUserTrainings(@RequestAttribute("user") User user,
                                              @Parameter(description = "The start date of the training.")
                                              @RequestParam(value = "startDate", required = false) String start,
                                              @Parameter(description = "The end date of the training.")
                                              @RequestParam(value = "endDate", required = false) String end) throws DateTimeParseException {
        if (start != null && end != null) {
            return trainingService.findByUserBetweenDates(user, LocalDate.parse(start), LocalDate.parse(end));
        }
        return trainingService.findAllByUser(user);
    }

    /**
     * Gets all trainings.
     *
     * @param user the user
     * @return the list of trainings
     * @throws AccessDeniedException if the user does not have the required privilege
     */
    @Operation(summary = "Get all trainings", description = "Gets all trainings.")
    @GetMapping("/all")
    public List<TrainingDto> getAllTrainings(@RequestAttribute("user") User user) throws AccessDeniedException {
        return trainingService.findAll(user);
    }

    /**
     * Gets a training by its id.
     *
     * @param user the user
     * @param id   the id of the training
     * @return the training
     * @throws TrainingDoesNotExistsException if the training does not exist
     */
    @Operation(summary = "Get training by id", description = "Gets a training by its id.")
    @GetMapping("/{id}")
    public TrainingDto getTraining(@RequestAttribute("user") User user,
                                   @Parameter(description = "The id of the training.")
                                   @PathVariable(name = "id") long id) throws TrainingDoesNotExistsException {
        return trainingService.find(user, id);
    }

    /**
     * Adds a training.
     *
     * @param user     the user
     * @param training the training
     * @return the training
     * @throws TrainingAlreadyExistsException if the training already exists
     */
    @Operation(summary = "Add training", description = "Adds a training.")
    @PostMapping()
    public TrainingDto addTraining(@RequestAttribute("user") User user,
                                   @RequestBody @Validated TrainingDto training) throws TrainingAlreadyExistsException {
        return trainingService.save(user, training);
    }

    /**
     * Updates a training.
     *
     * @param user     the user
     * @param id       the id of the training
     * @param training the training
     * @return the training
     * @throws TrainingDoesNotExistsException if the training does not exist
     */
    @Operation(summary = "Update training", description = "Updates a training.")
    @PutMapping("/{id}")
    public TrainingDto updateTraining(@RequestAttribute("user") User user,
                                      @Parameter(description = "The id of the training.")
                                      @PathVariable(name = "id") long id,
                                      @RequestBody @Validated TrainingDto training) throws TrainingDoesNotExistsException {
        return trainingService.update(user, id, training);
    }

    /**
     * Deletes a training.
     *
     * @param user the user
     * @param id   the id of the training
     * @throws TrainingDoesNotExistsException if the training does not exist
     */
    @Operation(summary = "Delete training", description = "Deletes a training.")
    @DeleteMapping("/{id}")
    public void deleteTraining(@RequestAttribute("user") User user,
                               @Parameter(description = "The id of the training.")
                               @PathVariable(name = "id") long id) throws TrainingDoesNotExistsException {
        trainingService.delete(user, id);
    }

}