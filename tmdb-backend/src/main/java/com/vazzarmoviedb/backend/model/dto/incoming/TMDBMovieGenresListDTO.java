package com.vazzarmoviedb.backend.model.dto.incoming;

import java.util.List;

public record TMDBMovieGenresListDTO(List<TMDBGenreNameIdDTO> genres) {
}
