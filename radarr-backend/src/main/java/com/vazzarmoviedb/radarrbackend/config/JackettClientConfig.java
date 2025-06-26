package com.vazzarmoviedb.radarrbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class JackettClientConfig {

    @Value("${jackett.api.host}")
    private String host;

    @Value("${jackett.api.port}")
    private int port;

    @Value("${jackett.api.key}")
    private String apiKey;

    @Bean
    public WebClient jackettWebClient() {
        String baseUrl = String.format("http://%s:%d/api/v2.0", host, port);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /** Helper method to build URL with API key param */
    public String appendApiKey(String uri) {
        if (uri.contains("?")) {
            return uri + "&apikey=" + apiKey;
        } else {
            return uri + "?apikey=" + apiKey;
        }
    }
}
