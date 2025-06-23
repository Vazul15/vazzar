package com.vazzarmoviedb.backend.service;

import com.vazzarmoviedb.backend.model.dto.incoming.TMDBGenreNameIdDTO;
import com.vazzarmoviedb.backend.model.dto.incoming.TMDBMovieGenresListDTO;
import com.vazzarmoviedb.backend.model.dto.response.GenreNameIdDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final WebClient webClient;

    @Value("${TMDB_API_KEY}")
    private String apiKey;


    public GenreService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public Mono<List<String>> getNameOfGenresById(List<Integer> ids) {
        return getGenreMap().map(genreMap ->
                ids.stream().map(genreMap::get).toList()
        );
    }

    public Mono<List<Integer>> getIdOfGenresByNames(List<String> names) {
        return getGenreMap().map(genreMap ->
                genreMap.entrySet().stream()
                        .filter(entry -> names.contains(entry.getValue()))
                        .map(entry -> entry.getKey())
                        .toList()
        );
    }

    public Mono<List<GenreNameIdDTO>> getGenres() {
        return getGenreMap().map(genreMap ->
                genreMap.entrySet().stream()
                        .map(entry -> new GenreNameIdDTO(entry.getValue(), entry.getKey())).toList()
                );
    }

    private Mono<Map<Integer, String>> getGenreMap() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/genre/movie/list")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(TMDBMovieGenresListDTO.class)
                .map(response -> response.genres().stream()
                        .collect(Collectors.toMap(TMDBGenreNameIdDTO::id, TMDBGenreNameIdDTO::name)));
    }
}
