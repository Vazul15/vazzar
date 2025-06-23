package com.vazzarmoviedb.backend.model.dto.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TMDBMovieGeneralDTO(
        Long id,
        String title,
        @JsonProperty("backdrop_path") String backdropPath,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("vote_average") Double voteAverage,
        Long popularity,
        @JsonProperty("genre_ids") List<Integer> genreIds,
        String overview
) {
    public String getBackdropUrl() {
        return backdropPath == null ? null : "https://image.tmdb.org/t/p/original" + backdropPath;
    }
    public String getPosterUrl() {
        return posterPath != null ? "https://image.tmdb.org/t/p/original" + posterPath : null;
    }
}