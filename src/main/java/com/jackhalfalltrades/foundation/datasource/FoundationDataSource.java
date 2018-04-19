package com.jackhalfalltrades.foundation.datasource;


import com.jackhalfalltrades.foundation.foundationConfig.PropertyManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@ConfigurationProperties
public class FoundationDataSource {

    private String driverClassName;

    private String url;

    private String userName;

    private String password;

    public FoundationDataSource() {
        this.driverClassName = PropertyManager.getProperty("spring.datasource.driver-class-name");
        this.url = PropertyManager.getProperty("spring.datasource.url");
        this.userName = PropertyManager.getProperty("spring.datasource.username");
        this.password = PropertyManager.getProperty("spring.datasource.password");
    }

    public FoundationDataSource(String driverClassName, String url, String userName, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.password = password;
        this.userName = userName;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(
                DataSourceBuilder.create()
                        .driverClassName(driverClassName)
                        .url(url)
                        .username(userName)
                        .password(password)
                        .build());
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }


}