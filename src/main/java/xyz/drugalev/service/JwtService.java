package xyz.drugalev.service;

import xyz.drugalev.entity.User;
import xyz.drugalev.util.JwtToken;

/**
 * Service for generating and parsing JWT tokens.
 */
public interface JwtService {
    /**
     * Generate JWT token for user.
     *
     * @param user user to generate token for
     * @return JWT token
     */
    JwtToken getJwtToken(User user);

    /**
     * Parse JWT token and return user.
     *
     * @param token JWT token to parse
     * @return user
     */
    User getUser(String token);
}