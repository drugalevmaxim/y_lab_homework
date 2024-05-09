package xyz.drugalev.config;

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
        type = SecuritySchemeType.APIKEY,
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