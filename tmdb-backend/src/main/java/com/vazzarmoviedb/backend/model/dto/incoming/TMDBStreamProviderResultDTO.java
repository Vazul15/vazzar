package com.vazzarmoviedb.backend.model.dto.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TMDBStreamProviderResultDTO(@JsonProperty("results") List<TMDBStreamProviderDTO> results) {
}
