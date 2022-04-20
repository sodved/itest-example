package com.sodved.itesteg.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VerticaConfig {

    @Bean("verticaDataSourceProperties")
    @ConfigurationProperties(prefix = "reporting.datasource") //mariadb
    public DataSourceProperties verticaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "verticaDataSource")
    @ConfigurationProperties(prefix = "reporting.datasource")
    public HikariDataSource verticaDataSource() {
        return (HikariDataSource) verticaDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "verticaJdbcTemplate")
    public JdbcTemplate verticaJdbcTemplate() {
        return new JdbcTemplate(verticaDataSource());
    }
}
