package xyz.drugalev.util;

import liquibase.UpdateSummaryEnum;
import liquibase.UpdateSummaryOutputEnum;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.command.core.helpers.ShowSummaryArgument;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * This class is responsible for handling database migrations using Liquibase.
 */
@Component
public class MigrationLoader {
    public MigrationLoader(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
             Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(jdbcTemplate.getDataSource().getConnection()));

            jdbcTemplate.update("CREATE SCHEMA IF NOT EXISTS ylab_trainings;");
            jdbcTemplate.update("CREATE SCHEMA IF NOT EXISTS ylab_service;");
            database.setDefaultSchemaName("ylab_trainings");
            database.setLiquibaseSchemaName("ylab_service");

            CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
            updateCommand.addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database);
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, "database/changelog.xml");
            updateCommand.addArgumentValue(ShowSummaryArgument.SHOW_SUMMARY, UpdateSummaryEnum.OFF);
            updateCommand .addArgumentValue(ShowSummaryArgument.SHOW_SUMMARY_OUTPUT, UpdateSummaryOutputEnum.LOG);
            updateCommand.execute();

        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException("Unable to migrate database: " + e.getMessage());
        }
    }
}
