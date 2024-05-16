package xyz.drugalev.ylabtrainingapp.service;

import xyz.drugalev.ylabtrainingapp.dto.JwtToken;
import xyz.drugalev.entity.User;

/**
 * JWT service.
 *
 * @author Drugalev Maxim
 */
public interface JwtService {
    /**
     * Generate JWT token for user.
     *
     * @param user user
     * @return JWT token
     */
    JwtToken getJwtToken(User user);

    /**
     * Get user from JWT token.
     *
     * @param token JWT token
     * @return user
     */
    User getUser(String token);
}