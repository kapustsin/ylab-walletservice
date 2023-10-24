package com.ylab.walletservice;

import com.ylab.walletservice.presentation.in.ConsoleProcessor;
import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import com.ylab.walletservice.repository.utils.ConnectionManager;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Main class representing the entry point of the Wallet application.
 */
public class Wallet {
    /**
     * Main method to start the Wallet application and initiate the console interface for user interaction.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        initDb();
        ConsoleProcessor processor = new ConsoleProcessor(new PlayerService(new JdbcPlayerRepository()),
                new TransactionService(new JdbcTransactionRepository(), new PlayerService(new JdbcPlayerRepository())));
        processor.start();
    }

    private static void initDb() {
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
        ) {
           // statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS walletservice");
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS liquibase");

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