package xyz.drugalev.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.service.JwtService;
import xyz.drugalev.util.JwtToken;
import xyz.drugalev.util.YamlPropertySourceFactory;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:/application.yml", factory = YamlPropertySourceFactory.class)
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;
    @Value("${spring.application.jwtTokenSecret}")
    private String jwtSigningKey;
    @Value("${spring.application.jwtTokenExpire}")
    private int expiresIn;

    public JwtToken getJwtToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSigningKey);
        return new JwtToken(JWT.create()
                .withSubject("User")
                .withClaim("id", user.getId())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(expiresIn))
                .sign(algorithm));
    }

    public User getUser(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSigningKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return userRepository.findById(decodedJWT.getClaim("id").asLong()).orElseThrow();
    }
}
