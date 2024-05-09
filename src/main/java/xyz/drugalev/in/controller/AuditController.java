package xyz.drugalev.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.UserPrivilegeException;
import xyz.drugalev.service.AuditService;

import java.util.List;

/**
 * Controller for audit.
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
     * @param user the user
     * @return the list of audits
     * @throws UserPrivilegeException if the user does not have the required privilege
     */
    @Operation(summary = "Get all audits", description = "Gets all audits for the current user.")
    @GetMapping
    public List<AuditDto> audit(@RequestAttribute("user") User user) throws UserPrivilegeException {
        return auditService.findAll(user);
    }
}