package com.vazzarmoviedb.backend.service;

import com.vazzarmoviedb.backend.model.dto.incoming.TMDBTopMoviesGeneralDTO;
import com.vazzarmoviedb.backend.model.dto.incoming.TMDBVideosOfMovieDTO;
import com.vazzarmoviedb.backend.model.dto.incoming.movieDetails.TMDBMovieCastDTO;
import com.vazzarmoviedb.backend.model.dto.incoming.movieDetails.TMDBMovieCreditsDTO;
import com.vazzarmoviedb.backend.model.dto.incoming.movieDetails.TMDBMovieDetailsDTO;
import com.vazzarmoviedb.backend.model.dto.response.*;
import com.vazzarmoviedb.backend.model.dto.response.movieDetails.*;
import com.vazzarmoviedb.backend.model.entity.Jobs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MovieService {
    private final WebClient webClient;
    private final GenreService genreService;
    private final WatchProviderService watchProviderService;

    @Value("${TMDB_API_KEY}")
    private String apiKey;


    public MovieService(WebClient.Builder webClientBuilder, GenreService genreService, WatchProviderService watchProviderService) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.genreService = genreService;
        this.watchProviderService = watchProviderService;
    }

    public Mono<TopMoviesGeneralDTO> getTopRatedMovies() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/top_rated")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "en-US")
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .bodyToMono(TMDBTopMoviesGeneralDTO.class)
                .flatMap(this::convertTmdbDataToRequiredReactive)
                .retry(3);
    }

    public Mono<TopMoviesGeneralDTO> getPopularMovies() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/popular")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "en-US")
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .bodyToMono(TMDBTopMoviesGeneralDTO.class)
                .flatMap(this::convertTmdbDataToRequiredReactive)
                .retry(3);
    }

    public Mono<TopMoviesGeneralDTO> getMoviesOfStreamingProviderByName(String streamingProviderName, String watchRegion) {
        return watchProviderService.getProviderIdByName(streamingProviderName, watchRegion)
                .flatMap(providerId -> {
                    if (providerId == null || providerId == -1) {
                        return Mono.empty();
                    }

                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/discover/movie")
                                    .queryParam("api_key", apiKey)
                                    .queryParam("watch_region", watchRegion)
                                    .queryParam("with_watch_providers", providerId)
                                    .queryParam("sort_by", "popularity.desc")
                                    .build())
                            .retrieve()
                            .bodyToMono(TMDBTopMoviesGeneralDTO.class)
                            .flatMap(this::convertTmdbDataToRequiredReactive)
                            .retry(3);
                });
    }

    public Mono<TopMoviesGeneralDTO> getMoviesOfStreamingProviderById(Integer streamingProviderId, String watchRegion) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("api_key", apiKey)
                        .queryParam("watch_region", watchRegion)
                        .queryParam("with_watch_providers", streamingProviderId)
                        .queryParam("sort_by", "popularity.desc")
                        .build())
                .retrieve()
                .bodyToMono(TMDBTopMoviesGeneralDTO.class)
                .flatMap(this::convertTmdbDataToRequiredReactive)
                .retry(3);


    }

    public Mono<TopMoviesGeneralDTO> getPopularMoviesByGenre(String genres, String watchRegion) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("api_key", apiKey)
                        .queryParam("watch_region", watchRegion)
                        .queryParam("with_genres", genres)
                        .queryParam("sort_by", "popularity.desc")
                        .build())
                .retrieve()
                .bodyToMono(TMDBTopMoviesGeneralDTO.class)
                .flatMap(this::convertTmdbDataToRequiredReactive)
                .retry(3);
    }

    public Mono<List<VideosOfMoviesDTO>> getVideosOfMovieById(Integer movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{movieId}/videos")
                        .queryParam("api_key", apiKey)
                        .build(movieId))
                .retrieve()
                .bodyToMono(TMDBVideosOfMovieDTO.class)
                .flatMap(this::convertTmdbDataToRequiredVideos)
                .retry(3);
    }

    public Mono<MovieDetailsAndCreditsDTO> getCompleteMovieDetails(Integer movieId) {
        Mono<MovieDetailsDTO> movieDetailsMono = getMovieDetailsByMovieId(movieId);
        Mono<MovieCreditsDTO> movieCreditsMono = getCreditsByMovieByMovieId(movieId);

        return Mono.zip(movieDetailsMono, movieCreditsMono)
                .map(tuple -> new MovieDetailsAndCreditsDTO(tuple.getT1(), tuple.getT2()));
    }

    private Mono<MovieDetailsDTO> getMovieDetailsByMovieId(Integer movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{movieId}")
                        .queryParam("api_key", apiKey)
                        .build(movieId))
                .retrieve()
                .bodyToMono(TMDBMovieDetailsDTO.class)
                .flatMap(this::convertMovieDetailsToRequiredReactive)
                .retry(3);
    }

    private Mono<MovieCreditsDTO> getCreditsByMovieByMovieId(Integer movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{movieId}/credits")
                        .queryParam("api_key", apiKey)
                        .build(movieId))
                .retrieve()
                .bodyToMono(TMDBMovieCreditsDTO.class)
                .flatMap(this::convertMovieCreditsToRequiredReactive)
                .retry(3);
    }

    private Mono<MovieCreditsDTO> convertMovieCreditsToRequiredReactive(TMDBMovieCreditsDTO movieCredits) {
        Mono<List<MovieCastDTO>> castMono = Flux.fromIterable(movieCredits.cast())
                .sort(Comparator.comparingInt(TMDBMovieCastDTO::order))
                .map(cast -> new MovieCastDTO(
                                cast.gender(),
                                cast.id(),
                                cast.known_for_department(),
                                cast.name(),
                                cast.original_name(),
                                cast.getProfilUrl(),
                                cast.character()
                        )
                ).collectList();

        Mono<List<MovieCrewDTO>> crewMono = Flux.fromIterable(movieCredits.crew())
                .map(crew -> new MovieCrewDTO(
                                crew.gender(),
                                crew.id(),
                                crew.known_for_department(),
                                crew.name(),
                                crew.original_name(),
                                crew.getProfilUrl(),
                                crew.credit_id(),
                                crew.department(),
                                crew.job()
                        )
                ).collectList()
                .flatMap(this::getDirectorOfMovie)
                ;

        return Mono.zip(castMono, crewMono).map(tuple -> new MovieCreditsDTO(tuple.getT1(), tuple.getT2()));

    }

    private Mono<List<MovieCrewDTO>> getDirectorOfMovie(List<MovieCrewDTO> crew) {
        return Mono.just(crew)
                .map(list -> list.stream()
                        .filter(director -> director.job().equals(Jobs.DIRECTOR.getJobTitle()))
                        .collect(Collectors.toList()));
    }


    private Mono<MovieDetailsDTO> convertMovieDetailsToRequiredReactive(TMDBMovieDetailsDTO movieDetails) {
        return Mono.just(movieDetails).map(movieDetail -> {
            List<ProductionCompaniesDTO> productionCompaniesDTOS = movieDetail.productionCompanies().stream().map(
                    productionCompany -> new ProductionCompaniesDTO(
                            productionCompany.id(),
                            productionCompany.getLogoUrl(),
                            productionCompany.name(),
                            productionCompany.originCountry()
                    )).toList();
            return new MovieDetailsDTO(
                    movieDetail.getBackdropUrl(),
                    movieDetail.budget(),
                    movieDetail.genres(),
                    movieDetail.homepage(),
                    movieDetail.id(),
                    movieDetail.originCountry(),
                    movieDetail.originalLanguage(),
                    movieDetail.originalTitle(),
                    movieDetail.overview(),
                    movieDetail.getPosterUrl(),
                    productionCompaniesDTOS,
                    movieDetail.releaseDate(),
                    movieDetail.revenue(),
                    movieDetail.runtime(),
                    movieDetail.status(),
                    movieDetail.title()
            );
        });
    }

    private Mono<List<VideosOfMoviesDTO>> convertTmdbDataToRequiredVideos(TMDBVideosOfMovieDTO videos) {
        return Flux.fromIterable(videos.results())
                .filter(video ->
                        video.official() && video.type().equals("Trailer") && video.site().equals("YouTube"))
                .map(video -> {
                            String youTubeLink = "https://www.youtube.com/embed/" + video.key();
                            return new VideosOfMoviesDTO(
                                    video.iso_639_1(),
                                    video.name(),
                                    video.size(),
                                    video.published_at(),
                                    youTubeLink
                            );
                        }

                ).collectList();
    }

    private Mono<TopMoviesGeneralDTO> convertTmdbDataToRequiredReactive(TMDBTopMoviesGeneralDTO movies) {
        return Flux.fromIterable(movies.generalMovies())
                .flatMap(movie -> genreService.getNameOfGenresById(movie.genreIds())
                        .map(genreNames -> new MovieGeneralDTO(
                                movie.id(),
                                movie.title(),
                                genreNames,
                                movie.overview(),
                                movie.getPosterUrl(),
                                movie.getBackdropUrl(),
                                movie.popularity()
                        )))
                .collectList()
                .map(moviesList -> {
                    moviesList.sort(Comparator
                            .comparing(MovieGeneralDTO::popularity).reversed()
                            .thenComparing(MovieGeneralDTO::title, Comparator.nullsLast(String::compareTo)));
                    return new TopMoviesGeneralDTO(movies.page(), moviesList);
                });
    }

}