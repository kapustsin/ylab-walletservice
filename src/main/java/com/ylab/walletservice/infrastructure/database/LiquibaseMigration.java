package com.ylab.walletservice.infrastructure.database;

import com.ylab.walletservice.configuration.utils.YamlFactory;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializing the database schema using Liquibase migrations.
 */
@PropertySources({
        @PropertySource(value = "classpath:application.yml", factory = YamlFactory.class)
})
public class LiquibaseMigration {
    final private DataSource dataSource;

    @Value("${liquibase.changeLogFile}")
    private String changeLogFile;

    @Value("${liquibase.liquibaseSchemaName}")
    private String liquibaseSchemaName;

    @Value("${liquibase.defaultSchemaName}")
    private String defaultSchemaName;

    /**
     * Constructs a new LiquibaseMigration with the specified DataSource.
     *
     * @param dataSource The DataSource instance representing the database connection pool.
     */
    public LiquibaseMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Initializes the database schema by applying Liquibase migrations.
     */
    @PostConstruct
    private void initDb() {
        final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS " + liquibaseSchemaName;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(SQL_CREATE_SCHEMA);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(liquibaseSchemaName);

            Liquibase liquibase = new Liquibase(changeLogFile,
                    new ClassLoaderResourceAccessor(), database);
            liquibase.getDatabase().setDefaultSchemaName(defaultSchemaName);
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}