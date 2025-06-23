package com.vazzarmoviedb.backend.model.dto.response.movieDetails;

import java.util.List;

public record MovieDetailsAndCreditsDTO(
        MovieDetailsDTO movieDetails,
        MovieCreditsDTO credits
) {
}
