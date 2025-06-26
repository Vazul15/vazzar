package com.vazzarmoviedb.radarrbackend.controller;

import com.vazzarmoviedb.radarrbackend.config.QBittorrentClientConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired private WebClient radarrClient;

    @Autowired private WebClient qbittorrentWebClient;

    @Autowired private WebClient jackettWebClient;

    @Autowired private com.vazzarmoviedb.radarrbackend.config.JackettClientConfig jackettConfig;
    @Autowired private QBittorrentClientConfig qBittorrentClientConfig;

    @GetMapping("/radarr")
    public Mono<ResponseEntity<String>> checkRadarr() {
        return radarrClient
                .get()
                .uri("system/status")
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> ResponseEntity.ok("Radarr OK"))
                .onErrorResume(
                        e -> Mono.just(ResponseEntity.status(503).body("Radarr Unreachable")));
    }

    @GetMapping("/qbittorrent")
    public Mono<ResponseEntity<String>> checkQbittorrent() {
        return qBittorrentClientConfig
                .login(qbittorrentWebClient)
                .flatMap(
                        cookie ->
                                qbittorrentWebClient
                                        .get()
                                        .uri("/app/version")
                                        .header(HttpHeaders.COOKIE, cookie)
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .map(body -> ResponseEntity.ok("qBittorrent OK")))
                .onErrorResume(
                        e ->
                                Mono.just(
                                        ResponseEntity.status(503)
                                                .body(
                                                        "qBittorrent Unreachable: "
                                                                + e.getMessage())));
    }

    @GetMapping("/jackett")
    public Mono<ResponseEntity<String>> checkJackett() {
        String uri = jackettConfig.appendApiKey("/indexers/all/results?Query=test");
        return jackettWebClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> ResponseEntity.ok("Jackett OK"))
                .onErrorResume(
                        e -> Mono.just(ResponseEntity.status(503).body("Jackett Unreachable")));
    }
}
