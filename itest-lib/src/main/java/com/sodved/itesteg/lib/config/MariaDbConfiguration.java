package com.sodved.itesteg.lib.config;

import com.zaxxer.hikari.HikariDataSource;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        basePackages = {"com.sodved.itesteg.lib.repository"}
)
public class MariaDbConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "metadata.datasource")
    public DataSourceProperties reportingDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "metadata.datasource")
    public HikariDataSource dataSource(DataSourceProperties mariadbDataSourceProperties) {
        return (HikariDataSource) mariadbDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        return builder.dataSource(dataSource)
                .packages("com.sodved.itesteg.lib.domain")
                .persistenceUnit("mariadb").properties(properties).build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory")
                                                                 EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager")
                                                           PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
