package xyz.drugalev.database;

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

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is responsible for handling database migrations using Liquibase.
 */
public class MigrationLoader {
    /**
     * Executes database migrations using Liquibase.
     */
    public static void migrate(){
        try (Connection connection = JDBCConnection.getConnection();
             Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))) {

            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS ylab_trainings").execute();
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS ylab_service").execute();

            database.setDefaultSchemaName("ylab_trainings");
            database.setLiquibaseSchemaName("ylab_service");

            CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
            updateCommand.addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database);
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, "database/changelog.xml");
            updateCommand.addArgumentValue(ShowSummaryArgument.SHOW_SUMMARY, UpdateSummaryEnum.OFF);
            updateCommand .addArgumentValue(ShowSummaryArgument.SHOW_SUMMARY_OUTPUT, UpdateSummaryOutputEnum.LOG);
            updateCommand.execute();

        } catch (LiquibaseException | SQLException e) {
            System.err.println("Unable to migrate database: " + e.getMessage());
        }
    }
}
