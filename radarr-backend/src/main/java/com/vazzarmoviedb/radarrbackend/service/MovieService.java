package com.vazzarmoviedb.radarrbackend.service;

import com.vazzarmoviedb.radarrbackend.config.radarr.RadarrProperties;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddMovieRequestDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.response.MovieToAddDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MovieService {
    private final WebClient radarrClient;
    private final RadarrProperties radarrProperties;

    public MovieService(WebClient radarrClient, RadarrProperties radarrProperties) {
        this.radarrClient = radarrClient;
        this.radarrProperties = radarrProperties;
    }

    public Mono<Void> addMovie(MovieToAddDTO movie) {
        int qualityProfileId = 1;

        AddMovieRequestDTO request = movie.toRadarrRequest(
                radarrProperties.getRootFolder(),
                qualityProfileId
        );

        return radarrClient.post()
                .uri("movie")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
