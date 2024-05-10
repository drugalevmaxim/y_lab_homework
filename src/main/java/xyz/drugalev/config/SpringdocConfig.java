package xyz.drugalev.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Springdoc.
 */
@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
@SecurityScheme(
        name = "bearerAuth",
        paramName = "Authorization",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpringdocConfig {
    /**
     * Gets the OpenAPI bean.
     *
     * @return the OpenAPI bean
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }
}