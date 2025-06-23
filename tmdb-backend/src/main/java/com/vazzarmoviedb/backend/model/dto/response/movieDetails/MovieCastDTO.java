package com.vazzarmoviedb.backend.model.dto.response.movieDetails;

public record MovieCastDTO(
        Integer gender,
        Integer id,
        String knownForDepartment,
        String name,
        String originalName,
        String profileUrl,
        String character
) {
}
