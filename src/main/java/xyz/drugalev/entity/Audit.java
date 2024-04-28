package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Audit entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    private User user;
    private String action;
    private LocalDateTime time;
}
