package com.ylab.walletservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ylab.walletservice.configuration.utils.YamlFactory;
import com.ylab.walletservice.infrastructure.database.LiquibaseMigration;
import com.ylab.walletservice.presentation.handler.TokenValidationInterceptor;
import com.ylab.walletservice.repository.PlayerRepository;
import com.ylab.walletservice.repository.TransactionRepository;
import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;
import com.ylab.walletservice.service.utils.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

/**
 * Configuration class for initializing the Spring application.
 */
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:application.yml", factory = YamlFactory.class)
})
public class Application implements WebApplicationInitializer {
    @Value("${common.url}")
    private String url;

    @Value("${common.username}")
    private String user;

    @Value("${common.password}")
    private String password;

    @Value("${jdbc.driver}")
    private String driver;

    /**
     * Configures the application when it starts up.
     *
     * @param servletContext ServletContext object representing the web application's runtime environment.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("com.ylab.walletservice");
        DispatcherServlet servlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic customDispatcherServlet = servletContext.addServlet("dispatcherServlet", servlet);
        customDispatcherServlet.setLoadOnStartup(1);
        customDispatcherServlet.addMapping("/");
    }

    /**
     * Creates a DataSource bean for database connection.
     *
     * @return Instance of DataSource.
     */

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    /**
     * Creates a JDBC player repository bean.
     *
     * @return Instance of JdbcPlayerRepository.
     */
    @Bean
    PlayerRepository jdbcPlayerRepository() {
        return new JdbcPlayerRepository(dataSource());
    }

    /**
     * Creates a JDBC transaction repository bean.
     *
     * @return Instance of JdbcTransactionRepository.
     */
    @Bean
    TransactionRepository jdbcTransactionRepository() {
        return new JdbcTransactionRepository(dataSource());
    }

    /**
     * Creates a player service bean.
     *
     * @return Instance of PlayerService.
     */
    @Bean
    PlayerService playerService() {
        return new PlayerService(jdbcPlayerRepository());
    }

    /**
     * Creates a transaction service bean.
     *
     * @return Instance of TransactionService.
     */
    @Bean
    TransactionService transactionService() {
        return new TransactionService(jdbcTransactionRepository(), playerService());
    }

    /**
     * Creates and configures an ObjectMapper bean.
     *
     * @return Instance of ObjectMapper.
     */
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    /**
     * Creates a JwtService bean.
     *
     * @return Instance of JwtService.
     */
    @Bean
    JwtService jwtService() {
        return new JwtService();
    }

    /**
     * Creates a TokenValidationInterceptor bean.
     *
     * @return Instance of TokenValidationInterceptor.
     */
    @Bean
    public TokenValidationInterceptor tokenValidationInterceptor() {
        return new TokenValidationInterceptor(jwtService(), objectMapper());
    }

    /**
     * Creates a LiquibaseMigration bean for managing database migrations.
     *
     * @return Instance of LiquibaseMigration.
     */
    @Bean
    public LiquibaseMigration liquibase() {
        return new LiquibaseMigration(dataSource());
    }
}