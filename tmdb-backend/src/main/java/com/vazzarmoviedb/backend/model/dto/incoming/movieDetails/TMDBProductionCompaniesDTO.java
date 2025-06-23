package com.vazzarmoviedb.backend.model.dto.incoming.movieDetails;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TMDBProductionCompaniesDTO(Integer id, String logoPath, String name, String originCountry) {
    public String getLogoUrl() {
        return logoPath == null ? null : "https://image.tmdb.org/t/p/original" + logoPath;
    }
}
