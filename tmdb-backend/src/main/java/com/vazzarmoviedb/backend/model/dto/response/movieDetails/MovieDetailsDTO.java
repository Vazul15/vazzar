package com.vazzarmoviedb.backend.model.dto.response.movieDetails;

import com.vazzarmoviedb.backend.model.dto.incoming.movieDetails.TMDBProductionCompaniesDTO;
import com.vazzarmoviedb.backend.model.dto.response.GenreNameIdDTO;

import java.util.List;

public record MovieDetailsDTO(
        String backDropUrl,
        Integer budget,
        List<GenreNameIdDTO> genres,
        String homepage,
        Integer id,
        List<String> originCountry,
        String originalLanguage,
        String originalTitle,
        String overview,
        String posterUrl,
        List<ProductionCompaniesDTO> productionCompanies,
        String releaseDate,
        Long revenue,
        Integer runtime,
        String status,
        String title
) {
}
