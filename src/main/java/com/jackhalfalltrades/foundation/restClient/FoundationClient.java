package com.jackhalfalltrades.foundation.restClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoundationClient {

//    private long maxIdleTime;
//
//    private int readTimeout;
//
//    private int connectionTimeout;

    @Autowired
    Environment environment;

    public RestTemplate getFoundationClient() {
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
        requestFactory.setConnectionRequestTimeout(Integer.parseInt(environment.getProperty("client.connection.timeout")));
        requestFactory.setReadTimeout(Integer.parseInt(environment.getProperty("client.connection.timeout")));
        final RestTemplate foundationClient = new RestTemplate(requestFactory);
        return foundationClient;
    }

    protected PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(Integer.MAX_VALUE);
        return poolingHttpClientConnectionManager;
    }


    protected HttpClient httpClient() {
        return HttpClientBuilder.create()
                .disableAutomaticRetries()
                .useSystemProperties()
                .setConnectionManager(poolingHttpClientConnectionManager())
                .evictExpiredConnections()
                .evictIdleConnections(Long.parseLong(environment.getProperty("client.connection.timeout")),
                        TimeUnit.MILLISECONDS)
                .build();
    }

}