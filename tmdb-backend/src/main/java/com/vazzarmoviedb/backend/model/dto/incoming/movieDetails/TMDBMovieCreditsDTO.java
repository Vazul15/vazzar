package com.vazzarmoviedb.backend.model.dto.incoming.movieDetails;

import java.util.List;

public record TMDBMovieCreditsDTO(Integer id, List<TMDBMovieCastDTO> cast, List<TMDBMovieCrewDTO> crew) {
}
