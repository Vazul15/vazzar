package com.vazzarmoviedb.backend.model.dto.incoming.movieDetails;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TMDBMovieCrewDTO(
        Integer gender,
        Integer id,
        String known_for_department,
        String name,
        String original_name,
        String profile_path,
        String credit_id,
        String department,
        String job
) {
    public String getProfilUrl() {
        return profile_path == null ? null : "https://image.tmdb.org/t/p/original" + profile_path;
    }
}
