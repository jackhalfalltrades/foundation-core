package com.jackhalfalltrades.foundation.restClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoundationClient {

    private long maxIdleTime;

    private int readTimeout;

    private int connectionTimeout;


    public RestTemplate getFoundationClient() {
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
        requestFactory.setConnectionRequestTimeout(connectionTimeout);
        requestFactory.setReadTimeout(readTimeout);
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
                .evictIdleConnections(maxIdleTime,
                        TimeUnit.MILLISECONDS)
                .build();
    }

}