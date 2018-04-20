package com.jackhalfalltrades.foundation.datasource;


import com.jackhalfalltrades.foundation.foundationConfig.PropertyManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

//@Configuration
//@ConfigurationProperties
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoundationDataSource {

    @Autowired
    private Environment environment;

//    private String driverClassName;
//
//    private String url;
//
//    private String userName;
//
//    private String password;

//    public FoundationDataSource() {
//        this.driverClassName = PropertyManager.getProperty("spring.datasource.driver-class-name");
//        this.url = PropertyManager.getProperty("spring.datasource.url");
//        this.userName = PropertyManager.getProperty("spring.datasource.username");
//        this.password = PropertyManager.getProperty("spring.datasource.password");
//    }

//    public FoundationDataSource(String driverClassName, String url, String userName, String password) {
//        this.driverClassName = driverClassName;
//        this.url = url;
//        this.password = password;
//        this.userName = userName;
//    }3666.00

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(
                DataSourceBuilder.create()
                        .driverClassName(environment.getProperty("spring.datasource.driver-class-name"))
                        .url(environment.getProperty("spring.datasource.url"))
                        .username(environment.getProperty("spring.datasource.username"))
                        .password(environment.getProperty("spring.datasource.password"))
                        .build());
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }


}