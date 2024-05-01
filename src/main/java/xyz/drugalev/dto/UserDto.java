package xyz.drugalev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Data transfer object for users.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private final Set<String> roles= new HashSet<>();
}
