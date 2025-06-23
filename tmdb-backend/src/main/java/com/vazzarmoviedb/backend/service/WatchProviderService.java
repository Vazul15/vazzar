package com.vazzarmoviedb.backend.service;

import com.vazzarmoviedb.backend.model.dto.incoming.TMDBStreamProviderDTO;
import com.vazzarmoviedb.backend.model.dto.incoming.TMDBStreamProviderResultDTO;
import com.vazzarmoviedb.backend.model.dto.response.StreamProviderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
public class WatchProviderService {
    WebClient webClient;

    @Value("${TMDB_API_KEY}")
    private String apiKey;

    public WatchProviderService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public Mono<Integer> getProviderIdByName(String name, String watchRegion) {
        return getMovieStreamProvidersOfTMDB(watchRegion)
                .map(providers -> providers.results().stream()
                        .filter(provider -> provider.providerName().contains(name))
                        .map(TMDBStreamProviderDTO::providerId)
                        .findFirst()
                        .orElse(-1));
    }

    public Mono<List<StreamProviderDTO>> getMovieStreamProvidersTransformed(String watchRegion) {
        return getMovieStreamProvidersOfTMDB(watchRegion)
                .map(providers -> providers.results().stream()
                        .sorted(Comparator.comparingInt(TMDBStreamProviderDTO::providerId))
                        .map(provider ->
                                new StreamProviderDTO(
                                        provider.providerName(),
                                        provider.getLogoPath(),
                                        provider.providerId()
                                ))
                        .toList());
    }

    private Mono<TMDBStreamProviderResultDTO> getMovieStreamProvidersOfTMDB(String watchRegion) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/watch/providers/movie")
                                .queryParam("api_key", apiKey)
                                .queryParam("watch_region", watchRegion)
                                .build())
                .retrieve()
                .bodyToMono(TMDBStreamProviderResultDTO.class)
                .retry(3)
                .onErrorResume(error -> {
                    System.err.println("Error during API call: " + error.getMessage());
                    return Mono.empty();
                });
    }
}
