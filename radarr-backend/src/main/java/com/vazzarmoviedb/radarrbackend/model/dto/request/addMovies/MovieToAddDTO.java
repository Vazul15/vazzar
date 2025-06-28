package com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies;

public record MovieToAddDTO(
        int tmdbId,
        String title,
        int year
) {
}
