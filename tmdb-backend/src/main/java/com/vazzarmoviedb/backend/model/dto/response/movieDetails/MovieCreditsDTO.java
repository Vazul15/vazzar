package com.vazzarmoviedb.backend.model.dto.response.movieDetails;

import java.util.List;

public record MovieCreditsDTO(List<MovieCastDTO> cast, List<MovieCrewDTO> crew) {
}
