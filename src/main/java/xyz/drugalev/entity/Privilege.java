package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Privilege entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {
    private long id;
    private String name;
}