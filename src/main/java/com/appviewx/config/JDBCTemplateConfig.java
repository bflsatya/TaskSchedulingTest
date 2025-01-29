package com.appviewx.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "local.mysql.db")
public class JDBCTemplateConfig {
    private String driver;
    private String url;
    private String username;
    private String password;


    @Bean
    public DataSource getDataSource() {
        final HikariDataSource ds = new HikariDataSource();

        ds.setMaximumPoolSize(1);
        ds.setDriverClassName(this.getDriver());
        ds.setJdbcUrl(getUrl());
        ds.setUsername(getUsername());
        ds.setPassword(getPassword());
        //ds.setInitializationFailFast(false);
        //ds.setAutoCommit(false);

        return ds;
    }

    @Bean
    public JdbcTemplate getJDBCTemplate() {
        return new JdbcTemplate(getDataSource());
    }


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
