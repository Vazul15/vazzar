package com.vazzarmoviedb.backend.model.dto.response.movieDetails;

public record MovieCrewDTO(
        Integer gender,
        Integer id,
        String knownForDepartment,
        String name,
        String originalName,
        String profileUrl,
        String creditId,
        String department,
        String job
) {
}
