package com.ylab.walletservice.configuration;

import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for initializing the Spring test application.
 */
@Configuration
public class ApplicationTest {
    /**
     * Creates a DataSource bean for database connection.
     *
     * @return Instance of DataSource.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(System.getProperty("TEST_CONTAINER_URL"));
        dataSource.setUsername(System.getProperty("TEST_CONTAINER_USERNAME"));
        dataSource.setPassword(System.getProperty("TEST_CONTAINER_PASSWORD"));
        return dataSource;
    }

    /**
     * Creates a JDBC player repository bean.
     *
     * @return Instance of JdbcPlayerRepository.
     */
    @Bean
    JdbcPlayerRepository jdbcPlayerRepository() {
        return new JdbcPlayerRepository(dataSource());
    }

    /**
     * Creates a JDBC transaction repository bean.
     *
     * @return Instance of JdbcTransactionRepository.
     */
    @Bean
    JdbcTransactionRepository jdbcTransactionRepository() {
        return new JdbcTransactionRepository(dataSource());
    }
}