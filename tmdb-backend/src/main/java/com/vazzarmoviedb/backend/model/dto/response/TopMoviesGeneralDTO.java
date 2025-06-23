package com.vazzarmoviedb.backend.model.dto.response;

import java.util.List;

public record TopMoviesGeneralDTO(Integer page, List<MovieGeneralDTO> movies) {
}
