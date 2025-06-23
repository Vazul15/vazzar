package com.vazzarmoviedb.backend.model.dto.response;

import java.util.List;

public record MovieGeneralDTO(
        Long id,
        String title,
        List<String> genres,
        String overview,
        String posterUrl,
        String backdropUrl,
        Long popularity
) {
}
