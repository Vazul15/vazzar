package com.vazzarmoviedb.backend.model.dto.incoming.movieDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vazzarmoviedb.backend.model.dto.response.GenreNameIdDTO;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TMDBMovieDetailsDTO(String backdropPath,
                                  Integer budget,
                                  List<GenreNameIdDTO> genres,
                                  String homepage,
                                  Integer id,
                                  String imdbId,
                                  List<String> originCountry,
                                  String originalLanguage,
                                  String originalTitle,
                                  String overview,
                                  Long popularity,
                                  String posterPath,
                                  List<TMDBProductionCompaniesDTO> productionCompanies,
                                  String releaseDate,
                                  Long revenue,
                                  Integer runtime,
                                  String status,
                                  String title
                                  ) {
    public String getBackdropUrl() {
        return backdropPath == null ? null : "https://image.tmdb.org/t/p/original" + backdropPath;
    }
    public String getPosterUrl() {
        return posterPath == null ? null : "https://image.tmdb.org/t/p/original" + posterPath;
    }
}
