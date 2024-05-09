package xyz.drugalev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import xyz.drugalev.util.YamlPropertySourceFactory;

import javax.sql.DataSource;

/**
 * Configuration class for JDBC.
 */
@Configuration
@PropertySource(value = "classpath:/application.yml", factory = YamlPropertySourceFactory.class)
public class JdbcConfig {
    /**
     * The driver class name.
     */
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    /**
     * The username.
     */
    @Value("${spring.datasource.username}")
    private String username;
    /**
     * The password.
     */
    @Value("${spring.datasource.password}")
    private String password;
    /**
     * The URL.
     */
    @Value("${spring.datasource.url}")
    private String url;
    /**
     * The schema.
     */
    @Value("${spring.datasource.schema}")
    private String schema;

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setSchema(schema);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    /**
     * Gets the property sources placeholder configurer.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}