package com.vazzarmoviedb.radarrbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RadarrClientConfig {

    @Value("${radarr.api.key}")
    private String radarrApiKey;

    @Value("${radarr.api.host}")
    private String radarrApiHost;

    @Value("${radarr.api.port}")
    private int radarrApiPort;

    @Bean
    public WebClient radarrClient() {
        String baseUrl = String.format("http://%s:%d/api/v3/", radarrApiHost, radarrApiPort);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(
                        headers -> {
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.set("X-Api-Key", radarrApiKey);
                        })
                .build();
    }
}
