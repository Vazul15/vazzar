package com.vazzarmoviedb.radarrbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vazzarmoviedb.radarrbackend.config.radarr.RadarrProperties;
import com.vazzarmoviedb.radarrbackend.exception.MovieNotFoundByIdException;
import com.vazzarmoviedb.radarrbackend.model.dto.incoming.ReleaseDataDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.internal.AddMovieDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.internal.ReleaseDownloadDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddOptionsDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.MovieToAddDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class MovieService {
    private final WebClient radarrClient;
    private final RadarrProperties radarrProperties;

    public MovieService(WebClient radarrClient, RadarrProperties radarrProperties) {
        this.radarrClient = radarrClient;
        this.radarrProperties = radarrProperties;
    }

    public Mono<Void> addMovie(MovieToAddDTO movie) {
        Boolean searchForMovies = true;
        AddMovieDTO movieDetailsToRadarrDownload = convertToRadarrRequest(movie, searchForMovies);

        return radarrClient.post()
                .uri("movie")
                .bodyValue(movieDetailsToRadarrDownload)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> addMovieWithPreferedQuality(MovieToAddDTO movieToAddDTO, Integer preferedQuality) throws MovieNotFoundByIdException {
        AddMovieDTO movieDetailsToRadarrDownload = convertToRadarrRequest(movieToAddDTO, false);

        return radarrClient.post()
                .uri("movie")
                .bodyValue(movieDetailsToRadarrDownload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(response -> {
                    if (response.hasNonNull("id") && response.get("id").asInt() > 0) {
                        int movieRadarrId = response.get("id").asInt();
                        return addReleaseToDownload(movieRadarrId, preferedQuality);
                    } else {
                        return Mono.error(new MovieNotFoundByIdException("Radarr did not fount active movieId"));
                    }
                });
    }


    private Mono<Void> addReleaseToDownload(Integer movieId, Integer preferedQuality) {
        Mono<ReleaseDataDTO> getBestReleaseByQuality = getBestReleaseByQuality(movieId, preferedQuality);

        return getBestReleaseByQuality(movieId, preferedQuality)
                .flatMap(release -> radarrClient.post()
                        .uri("release")
                        .bodyValue(new ReleaseDownloadDTO(
                                release.guid(),
                                release.indexerId(),
                                movieId))
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    ;

    private AddMovieDTO convertToRadarrRequest(MovieToAddDTO movieToAddDTO, Boolean searchForMovie) {
        AddOptionsDTO optionsDTO = new AddOptionsDTO(searchForMovie);
        String titleSlug = movieToAddDTO.title().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + movieToAddDTO.year();
        String rootFolderPath = radarrProperties.getRootFolder();
        String path = rootFolderPath + "/" + movieToAddDTO.title() + " (" + movieToAddDTO.year() + ")";
        int qualityProfileId = 1;

        return new AddMovieDTO(
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

    private Mono<ReleaseDataDTO> getBestReleaseByQuality(Integer movieId, Integer preferedQuality) {
        int minSeeders = 5;

        return radarrClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("release")
                        .queryParam("movieId", movieId)
                        .build())
                .retrieve()
                .bodyToFlux(ReleaseDataDTO.class)
                .filter(release ->
                        preferedQuality.equals(release.quality().quality().resolution()) &&
                                Boolean.FALSE.equals(release.rejected()) &&
                                //Boolean.TRUE.equals(release.downloadAllowed()) &&
                                release.seeders() >= minSeeders)
                .sort(Comparator.comparingInt(ReleaseDataDTO::seeders).reversed())
                .next();
    }

}
