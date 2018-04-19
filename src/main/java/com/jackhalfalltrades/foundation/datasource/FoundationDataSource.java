package com.jackhalfalltrades.foundation.datasource;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

    @ConfigurationProperties(prefix = "spring.datasource")
    @Configuration
    public class FoundationDataSource {

        private NamedParameterJdbcTemplate getSlamDBTemplate() {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create().build());
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }
    }