package com.ylab.walletservice.infrastructure.database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializing the database schema using Liquibase migrations.
 */
public class LiquibaseMigration {
    final private DataSource dataSource;

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
        final String SQL_CREATE_SCHEMA = """
                CREATE SCHEMA IF NOT EXISTS liquibase
                """;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(SQL_CREATE_SCHEMA);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("liquibase");

            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.getDatabase().setDefaultSchemaName("walletservice");
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}