package com.vazzarmoviedb.radarrbackend.service;

import com.vazzarmoviedb.radarrbackend.config.radarr.RadarrProperties;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddMovieRequestDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddOptionsDTO;
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

        AddMovieRequestDTO movieDetailsToRadarrDownload = convertToRadarrRequest(movie);

        return radarrClient.post()
                .uri("movie")
                .bodyValue(movieDetailsToRadarrDownload)
                .retrieve()
                .bodyToMono(Void.class);
    }


    private AddMovieRequestDTO convertToRadarrRequest(MovieToAddDTO movieToAddDTO) {
        AddOptionsDTO optionsDTO = new AddOptionsDTO(false);
        String titleSlug = movieToAddDTO.title().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + movieToAddDTO.year();
        String rootFolderPath = radarrProperties.getRootFolder();
        String path = rootFolderPath + "/" + movieToAddDTO.title() + " (" + movieToAddDTO.year() + ")";
        int qualityProfileId = 1;

        return new AddMovieRequestDTO(
                movieToAddDTO.tmdbId(),
                movieToAddDTO.title(),
                movieToAddDTO.year(),
                qualityProfileId,
                rootFolderPath,
                true,
                optionsDTO,
                titleSlug,
                path
        );
    }

}
