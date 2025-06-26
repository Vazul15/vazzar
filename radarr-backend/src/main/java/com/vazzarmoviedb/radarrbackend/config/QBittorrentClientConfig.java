package com.vazzarmoviedb.radarrbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class QBittorrentClientConfig {

    @Value("${qbittorrent.api.host}")
    private String host;

    @Value("${qbittorrent.api.port}")
    private int port;

    @Value("${qbittorrent.api.user}")
    private String username;

    @Value("${qbittorrent.api.password}")
    private String password;

    @Bean
    public WebClient qbittorrentWebClient() {
        String baseUrl = String.format("http://%s:%d/api/v2", host, port);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    // Separate method to perform login and return Mono<String> cookie
    public Mono<String> login(WebClient client) {
        return client.post()
                .uri("/auth/login")
                .bodyValue("username=" + username + "&password=" + password)
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return Mono.justOrEmpty(
                                        response.headers()
                                                .asHttpHeaders()
                                                .getFirst(HttpHeaders.SET_COOKIE));
                            } else {
                                return Mono.error(
                                        new RuntimeException(
                                                "Failed to authenticate with qBittorrent"));
                            }
                        });
    }
}
