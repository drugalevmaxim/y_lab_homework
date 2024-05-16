package xyz.drugalev.ylabtrainingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.drugalev.ylabtrainingapp.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;
import xyz.drugalev.ylabtrainingapp.service.AuditService;

import java.util.List;

/**
 * Audit controller.
 *
 * @author Drugalev Maxim
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;
    /**
     * Gets all audits.
     *
     * @param user the current user
     * @return a list of audit DTOs
     * @throws UserPrivilegeException if the user does not have the required privilege
     */
    @Operation(summary = "Get all audits", description = "Gets all audits for the current user.")
    @GetMapping
    public List<AuditDto> getAudits(@RequestAttribute("user") User user) throws UserPrivilegeException {
        return auditService.findAll(user);
    }
}