package com.vazzarmoviedb.radarrbackend.controller;

import com.vazzarmoviedb.radarrbackend.model.dto.incoming.ReleaseDataDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.MovieToAddDTO;
import com.vazzarmoviedb.radarrbackend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/movie")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/")
    public Mono<Void> addMovieToRadarr(@RequestBody MovieToAddDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }

    @PostMapping("/quality")
    public Mono<Void> movieReleasesData(@RequestBody MovieToAddDTO movieDto, @RequestParam Integer preferedQuality) {
        return movieService.addMovieWithPreferedQuality(movieDto, preferedQuality);
    }
}
