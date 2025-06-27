package com.vazzarmoviedb.radarrbackend.config;

import com.vazzarmoviedb.radarrbackend.config.radarr.RadarrProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    private final RadarrProperties radarr;

    @Autowired
    public WebClientConfig(RadarrProperties radarr) {
        this.radarr = radarr;
    }

    @Bean
    public WebClient radarrClient() {
        String baseUrl = String.format("http://%s:%d/api/v3/", radarr.getHost(), radarr.getPort());

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("X-Api-Key", radarr.getApiKey());
                })
                .build();
    }

    @Bean
    public String getRadarrRootFolder() {
        return radarr.getRootFolder();
    }

}
