package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role entity.
 *
 * @author Drugalev Maxim
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private long id;
    private String name;
}
