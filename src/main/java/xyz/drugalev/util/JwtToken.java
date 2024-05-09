package xyz.drugalev.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class representing a JWT token.
 */
@Data
@AllArgsConstructor
public class JwtToken {
    /**
     * The token.
     */
    private String token;
}