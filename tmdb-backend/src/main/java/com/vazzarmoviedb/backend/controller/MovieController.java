package com.vazzarmoviedb.backend.controller;

import com.vazzarmoviedb.backend.model.dto.response.*;
import com.vazzarmoviedb.backend.model.dto.response.movieDetails.MovieDetailsAndCreditsDTO;
import com.vazzarmoviedb.backend.model.dto.response.movieDetails.MovieDetailsDTO;
import com.vazzarmoviedb.backend.service.GenreService;
import com.vazzarmoviedb.backend.service.MovieService;
import com.vazzarmoviedb.backend.service.WatchProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final WatchProviderService watchProviderService;
    private final GenreService genreService;

    public MovieController(MovieService movieService, WatchProviderService watchProviderService, GenreService genreService) {
        this.movieService = movieService;
        this.watchProviderService = watchProviderService;
        this.genreService = genreService;
    }

    @GetMapping("/top-movies")
    public Mono<ResponseEntity<TopMoviesGeneralDTO>> getTopMovies() {
        return movieService.getTopRatedMovies()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/popular")
    public Mono<ResponseEntity<TopMoviesGeneralDTO>> getPopularMovies() {
        return movieService.getPopularMovies().map(ResponseEntity::ok);
    }

    @GetMapping("")
    public Mono<ResponseEntity<TopMoviesGeneralDTO>> getMoviesByStreamingProviderByName(@RequestParam String providerName, @RequestParam String watchRegion) {
        return movieService.getMoviesOfStreamingProviderByName(providerName, watchRegion).map(ResponseEntity::ok);
    }

    @GetMapping("/id")
    public Mono<ResponseEntity<TopMoviesGeneralDTO>> getMoviesByStreamingProviderById(@RequestParam Integer providerId, @RequestParam String watchRegion) {
        return movieService.getMoviesOfStreamingProviderById(providerId, watchRegion).map(ResponseEntity::ok);
    }

    @GetMapping("/providers")
    public Mono<ResponseEntity<List<StreamProviderDTO>>> getStreamProviders(@RequestParam String watchRegion) {
        return watchProviderService.getMovieStreamProvidersTransformed(watchRegion).map(ResponseEntity::ok);
    }

    @GetMapping("/genre")
    public Mono<ResponseEntity<TopMoviesGeneralDTO>> getMoviesByGenres(@RequestParam String watchRegion,
                                                                       @RequestParam String genres) {
        return movieService.getPopularMoviesByGenre(genres, watchRegion).map(ResponseEntity::ok);
    }

    @GetMapping("/genres")
    public Mono<ResponseEntity<List<GenreNameIdDTO>>> getGenres() {
        return genreService.getGenres().map(ResponseEntity::ok);
    }

    @GetMapping("/trailer")
    public Mono<ResponseEntity<List<VideosOfMoviesDTO>>> getTrailersOfMovie(@RequestParam Integer movieId) {
        return movieService.getVideosOfMovieById(movieId).map(ResponseEntity::ok);
    }

    @GetMapping("/movie-details")
    public Mono<ResponseEntity<MovieDetailsAndCreditsDTO>> getMovieDetails(@RequestParam Integer movieId) {
        return movieService.getCompleteMovieDetails(movieId).map(ResponseEntity::ok);
    }
}
