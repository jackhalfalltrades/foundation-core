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
@NoArgsConstructor
@Builder
public class FoundationClient {

    private RestTemplate getFoundationClient(long maxIdleTime, int readTimeout, int connectionTimeout) {
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient(maxIdleTime));
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


    protected HttpClient httpClient(long maxIdleTime) {
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