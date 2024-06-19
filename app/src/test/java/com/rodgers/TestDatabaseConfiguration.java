package com.rodgers;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.testcontainers.containers.wait.strategy.WaitAllStrategy.Mode.WITH_MAXIMUM_OUTER_TIMEOUT;

@Configuration
public class TestDatabaseConfiguration {

    @Container
    public static final PostgreSQLContainer postgreSQLContainer =new PostgreSQLContainer("postgres:latest");

    static {
        postgreSQLContainer.withExposedPorts(5432);
        postgreSQLContainer.withDatabaseName("tdlib_client")
                .withUsername("postgres")
                .withPassword("admin");
        postgreSQLContainer.waitingFor(
                new WaitAllStrategy(WITH_MAXIMUM_OUTER_TIMEOUT)
                        .withStartupTimeout(Duration.ofSeconds(90))
                        .withStrategy(Wait.forListeningPort())
        );
        postgreSQLContainer.start();
        System.setProperty("spring.r2dbc.url","r2dbc:postgresql://"+postgreSQLContainer.getHost()+":"+postgreSQLContainer.getMappedPort(5432).toString()+"/tdlib_client");
        System.setProperty("spring.r2dbc.username","postgres");
        System.setProperty("spring.r2dbc.password","admin");


    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}