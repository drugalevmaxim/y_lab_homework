package xyz.drugalev.ylabtrainingapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Audit DTO.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Audit record")
public class AuditDto {
    private String user;
    private String action;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
