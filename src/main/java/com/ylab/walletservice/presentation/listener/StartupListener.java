package com.ylab.walletservice.presentation.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import com.ylab.walletservice.repository.utils.ConnectionManager;
import com.ylab.walletservice.service.LogService;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;
import com.ylab.walletservice.service.utils.JwtService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StartupListener implements ServletContextListener {
    private static void initDb() {
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
        ) {
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

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initDb();
        PlayerService playerService = new PlayerService(new JdbcPlayerRepository());
        TransactionService transactionService = new TransactionService(new JdbcTransactionRepository(), playerService);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JwtService jwtService = new JwtService();
        ServletContext context = event.getServletContext();
        context.setAttribute("playerService", playerService);
        context.setAttribute("transactionService", transactionService);
        context.setAttribute("objectMapper", objectMapper);
        context.setAttribute("jwtService", jwtService);
        LogService.add("Server startup successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}