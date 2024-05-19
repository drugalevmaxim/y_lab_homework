package xyz.drugalev.ylabtrainingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import xyz.drugalev.ylabauditspringbootstarter.annotation.EnableAudit;

/**
 * Test configuration for YlabTrainingAppApplication.
 *
 * @author Drugalev Maxim
 */
@TestConfiguration(proxyBeanMethods = false)
@EnableAudit
public class TestYlabTrainingAppApplicationConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2-alpine3.19"));
    }

    public static void main(String[] args) {
        SpringApplication.from(YlabTrainingAppApplication::main).with(TestYlabTrainingAppApplicationConfiguration.class).run(args);
    }

}
