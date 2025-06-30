package com.vazzarmoviedb.radarrbackend.model.dto.internal;

import com.vazzarmoviedb.radarrbackend.model.dto.request.addMovies.AddOptionsDTO;

public record AddMovieDTO(
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
