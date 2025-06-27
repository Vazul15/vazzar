package com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies;

public record AddMovieRequestDTO(
        int tmdbId,
        String title,
        int year,
        int qualityProfileId,
        String rootFolderPath,
        boolean monitored,
        AddOptionsDTO addOptions,
        String titleSlug,
        String path
) {
}
