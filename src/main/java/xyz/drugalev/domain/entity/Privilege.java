package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User privilege entity.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class Privilege {
    private final int id;
    private final String name;
}