package com.vazzarmoviedb.radarrbackend.controller;

import com.vazzarmoviedb.radarrbackend.model.dto.response.MovieToAddDTO;
import com.vazzarmoviedb.radarrbackend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/movie")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public Mono<Void> addMovieToRadarr(@RequestBody MovieToAddDTO movieDTO) {
        return movieService.addMovie(movieDTO);
    }
}
