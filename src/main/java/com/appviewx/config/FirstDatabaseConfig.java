package com.appviewx.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "primaryDbEntityManagerFactory",
        transactionManagerRef = "primaryDbTransactionManager",
        basePackages = {"com.appviewx.repos.primarydb"})
public class FirstDatabaseConfig {
    @Primary
    @Bean(name = "primaryDbDataSource")
    @ConfigurationProperties(prefix = "first.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryDbEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                                @Qualifier("primaryDbDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.appviewx.model.primarydb")
                .build();
    }

    @Primary
    @Bean(name = "primaryDbTransactionManager")
    public PlatformTransactionManager primaryDbTransactionManager(
            @Qualifier("primaryDbEntityManagerFactory") EntityManagerFactory primaryDbEntityManagerFactory) {

        return new JpaTransactionManager(primaryDbEntityManagerFactory);
    }

}
