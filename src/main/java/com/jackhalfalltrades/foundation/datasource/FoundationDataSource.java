package com.jackhalfalltrades.foundation.datasource;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

    @Data
    @Builder
    @NoArgsConstructor
    @ConfigurationProperties(prefix = "spring.datasource")
    @Configuration
    public class FoundationDataSource {

        private NamedParameterJdbcTemplate getDBTemplate() {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create().build());
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }
    }