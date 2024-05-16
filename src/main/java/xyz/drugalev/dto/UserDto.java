package xyz.drugalev.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class    UserDto {
    @NotNull
    @Size(min=2, max=32, message = "Username must contain at least 2 characters and no more than 32")
    private String username;
    @NotNull
    @Size(min = 6, max = 64, message = "Password must contain at least 6 characters and no more than 64")
    private String password;
}