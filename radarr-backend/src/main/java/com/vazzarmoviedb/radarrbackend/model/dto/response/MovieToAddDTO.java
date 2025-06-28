package com.vazzarmoviedb.radarrbackend.model.dto.response;

public record MovieToAddDTO(
        int tmdbId,
        String title,
        int year
) {
}
