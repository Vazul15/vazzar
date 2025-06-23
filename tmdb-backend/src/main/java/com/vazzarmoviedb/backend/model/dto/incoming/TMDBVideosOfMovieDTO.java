package com.vazzarmoviedb.backend.model.dto.incoming;

import java.util.List;

public record TMDBVideosOfMovieDTO(Integer movieId, List<TMDBVideosOfMoviesResultsDTO> results) {
}
