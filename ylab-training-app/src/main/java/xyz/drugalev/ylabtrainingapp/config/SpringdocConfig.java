package xyz.drugalev.ylabtrainingapp.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc configuration class.
 *
 * @author Drugalev Maxim
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        paramName = "Authorization",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpringdocConfig {
}
