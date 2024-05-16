package xyz.drugalev.ylabtrainingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JWT token DTO.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
@Schema(name = "JWT Auth Token")
public class JwtToken {
    private String token;
}